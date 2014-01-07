package org.flyingfortress.test.txn;

import org.flyingfortress.api.Destination;
import org.flyingfortress.api.EMITOR_TYPE;
import org.flyingfortress.api.Message;
import org.flyingfortress.core.config.KafkaConsumerConfig;
import org.flyingfortress.core.config.KafkaProducerConfig;
import org.flyingfortress.core.config.TransactionConfiguration;
import org.flyingfortress.core.subscribe.Subscriber;
import org.flyingfortress.core.txn.MessagePublisher;
import org.flyingfortress.core.txn.MessageSubscriber;
import org.flyingfortress.core.txn.MessageTransaction;
import org.flyingfortress.core.txn.TransactionManager;
import org.flyingfortress.exception.subscribe.SubscriberException;
import org.flyingfortress.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 31/12/13
 * Time: 6:22 PM
 */
public class KafkaTransactionTest extends BaseTest {

    Destination pickup = new Destination("KafkaTransactionTest");
    TransactionConfiguration configuration ;

    @Before
    public void setUp() throws Exception {
        configuration = new TransactionConfiguration();
        configuration.setKafkaProducerConfig(new KafkaProducerConfig());
        configuration.setKafkaConsumerConfig(new KafkaConsumerConfig());
        TransactionManager.init(configuration);
        try {
            //do reset.
            MessageTransaction.rollback();
        } catch (IllegalStateException ingore) {
        }
    }

    @Test
    public void testSimpleTransaction() throws Exception {

        String id =   "testSimpleTransaction_SubId_"+System.currentTimeMillis();
        String payload = "testSimpleTransaction";
        MessageSubscriber messageSubscriber = new MessageSubscriber(id,id,pickup,configuration.getKafkaConsumerConfig());
        TestSubscriber testSubscriber = new TestSubscriber(payload);
        messageSubscriber.registerCallback(testSubscriber);
        messageSubscriber.startup();
        Thread.sleep(2000);//wait for subscriber to start.

        MessageTransaction.start();
        MessagePublisher.addMessage(new Message(payload), pickup);
        MessagePublisher.addMessage(new Message(payload), pickup);
        MessageTransaction.commit();
        Thread.sleep(2000);//wait for subscriber to get it.
        Assert.assertEquals(2,testSubscriber.countMsgReceived);

    }

    private class TestSubscriber implements Subscriber{
        private final String payload;
        private int countMsgReceived = 0;

        private TestSubscriber(String payload) {
            this.payload = payload;
        }

        private boolean msgRecieved = false;
        @Override
        public <M> void handleMessage(M message) throws SubscriberException {
            if(((String)((Message)message).getBody()).contains(payload)){
                msgRecieved=true;
                countMsgReceived++;
            }
        }

        @Override
        public void registerCallback(Subscriber subscriber) {

        }

        @Override
        public String getId() {
            return "TestSubscriber";
        }
    }
}
