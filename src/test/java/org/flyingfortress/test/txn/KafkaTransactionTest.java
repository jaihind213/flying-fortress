package org.flyingfortress.test.txn;

import org.flyingfortress.api.Destination;
import org.flyingfortress.api.Message;
import org.flyingfortress.core.config.KafkaConsumerConfig;
import org.flyingfortress.core.config.KafkaProducerConfig;
import org.flyingfortress.core.config.TransactionConfiguration;
import org.flyingfortress.core.subscribe.Subscriber;
import org.flyingfortress.core.txn.MessagePublisher;
import org.flyingfortress.core.txn.MessageSubscriber;
import org.flyingfortress.core.txn.MessageTransaction;
import org.flyingfortress.core.txn.TransactionManager;
import org.flyingfortress.exception.LifeCycleException;
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

    Destination pickup_1 = new Destination("KafkaTransactionTest_1");
    Destination pickup_2 = new Destination("KafkaTransactionTest_2");
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

        TestSubscriber testSubscriber = createTestSubscriber(pickup_1,id,payload);

        Thread.sleep(2000);//wait for subscriber to start.

        MessageTransaction.start();
        MessagePublisher.addMessage(new Message(payload), pickup_1);
        MessagePublisher.addMessage(new Message(payload), pickup_1);
        MessageTransaction.commit();

        Thread.sleep(2000);//wait for subscriber to get it.
        Assert.assertEquals(2,testSubscriber.countMsgReceived);

    }

    @Test
    public void testMultiTopicTransaction() throws Exception {

        String id_1 =   "testMultiTopicTransaction_SubId1_"+System.currentTimeMillis();
        String payload_1 = "testMultiTopicTransaction_payload_1";

        String id_2 =   "testMultiTopicTransaction_SubId2_"+System.currentTimeMillis();
        String payload_2 = "testMultiTopicTransaction_payload_2";

        TestSubscriber testSubscriber_1 = createTestSubscriber(pickup_1,id_1,payload_1);
        Thread.sleep(2000);//wait for subscriber to start.
        TestSubscriber testSubscriber_2 = createTestSubscriber(pickup_2,id_2,payload_2);
        Thread.sleep(2000);//wait for subscriber to start.

        MessageTransaction.start();
        MessagePublisher.addMessage(new Message(payload_1), pickup_1);
        MessagePublisher.addMessage(new Message(payload_2), pickup_2);
        MessagePublisher.addMessage(new Message(payload_1), pickup_1);
        MessageTransaction.commit();
        Thread.sleep(2000);//wait for subscriber to get it.
        Assert.assertEquals(2,testSubscriber_1.countMsgReceived);
        Assert.assertEquals(1,testSubscriber_2.countMsgReceived);

    }


    private class TestSubscriber implements Subscriber{
        private final String expectedPayload; //predefined expectedPayload its meant to receive for the tests!
        private final String id;
        private int countMsgReceived = 0;

        private TestSubscriber(String payload) {
            this.expectedPayload = payload;
            this.id =  "TestSubscriber:"+System.currentTimeMillis();
        }

        private TestSubscriber(String payload,String id) {
            this.expectedPayload = payload;
            this.id =  "TestSubscriber:"+id;
        }

        private boolean msgRecieved = false;
        @Override
        public <M> void handleMessage(M message) throws SubscriberException {
            if(((String)((Message)message).getBody()).contains(expectedPayload)){
                msgRecieved=true;
                countMsgReceived++;
            }
        }

        @Override
        public void registerCallback(Subscriber subscriber) {

        }

        @Override
        public String getId() {
            return id;
        }
    }

    private TestSubscriber createTestSubscriber(Destination destination,String id, String expectedPayload) throws LifeCycleException {
        MessageSubscriber messageSubscriber = new MessageSubscriber(id,id, destination,configuration.getKafkaConsumerConfig());
        TestSubscriber testSubscriber = new TestSubscriber(expectedPayload,id);
        messageSubscriber.registerCallback(testSubscriber);
        messageSubscriber.startup();
        return testSubscriber;
    }
}
