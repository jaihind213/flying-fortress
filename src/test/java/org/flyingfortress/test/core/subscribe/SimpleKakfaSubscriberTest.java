package org.flyingfortress.test.core.subscribe;

import org.flyingfortress.api.Destination;
import org.flyingfortress.api.Message;
import org.flyingfortress.core.config.KafkaConsumerConfig;
import org.flyingfortress.core.config.KafkaProducerConfig;
import org.flyingfortress.core.emit.KafkaEmitor;
import org.flyingfortress.core.subscribe.SimpleKafkaSubscriber;
import org.flyingfortress.core.subscribe.Subscriber;
import org.flyingfortress.exception.LifeCycleException;
import org.flyingfortress.exception.emit.EmitorException;
import org.flyingfortress.exception.subscribe.SubscriberException;
import org.flyingfortress.test.BaseTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;


/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 31/12/13
 * Time: 5:03 PM
 */
public class SimpleKakfaSubscriberTest extends BaseTest implements Subscriber{

    private Destination destination = new Destination("SimpleKakfaSubscriberTest");
    private SimpleKafkaSubscriber simpleKafkaSubscriber;
    private KafkaEmitor kafkaEmitor;
    private volatile boolean testMsgRecieved=false;
    private String testPayload = "SimpleKakfaSubscriberTest";
    private CountDownLatch msgWaitLatch = new CountDownLatch(1);
    private LoopProducer loopProducer;

    @Before
    public void setUp() throws Exception {
        //todo: delete old Q and start afresh   https://issues.apache.org/jira/browse/KAFKA-330
        KafkaConsumerConfig config = new KafkaConsumerConfig();
        final String id = "SimpleKakfaSubscriberTest_"+System.currentTimeMillis();
        simpleKafkaSubscriber = new SimpleKafkaSubscriber(id, id, destination, config);
        loopProducer = new LoopProducer();
        loopProducer.start();
        simpleKafkaSubscriber.registerCallback(this);
        simpleKafkaSubscriber.startup();
    }

    @After
    public void tearDown() throws Exception {
        //todo: delete old Q and start afresh    https://issues.apache.org/jira/browse/KAFKA-330
        try {
            loopProducer.shutdown();
        } catch (LifeCycleException ignore) {
        }
        try {
            simpleKafkaSubscriber.shutdown(); //this prints java.nio.channels.ClosedByInterruptException
        } catch (LifeCycleException ignore) { }
    }

    @Test(timeout = 1000)
    public void testSubscription() throws Exception {
        long start = System.currentTimeMillis();
        msgWaitLatch.await();
        Assert.assertTrue(testMsgRecieved);
        System.out.println("Time taken for test:"+(System.currentTimeMillis()-start));
    }

    @Override
    public <M> void handleMessage(M message) throws SubscriberException {
        if(((String)message).contains(testPayload)){
            msgWaitLatch.countDown();
            testMsgRecieved = true;
        }
    }

    @Override
    public void registerCallback(Subscriber subscriber) { }

    @Override
    public String getId() {
        return "FlyingSubscriber";
    }

    private class LoopProducer extends Thread{
        LoopProducer() throws LifeCycleException {
            this.setDaemon(true);
            KafkaProducerConfig kafkaProducerConfig = new KafkaProducerConfig();
            kafkaEmitor = new KafkaEmitor(kafkaProducerConfig,"PublisherForSimpleKakfaSubscriberTest");
            kafkaEmitor.startup();
        }

        volatile boolean loop = true;
        @Override
        public void run() {
            while(loop)
            {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
                try {
                    kafkaEmitor.emit(new Message(testPayload), destination);
                } catch (EmitorException e) {
                    e.printStackTrace();
                }
            }
        }

        public void shutdown() throws LifeCycleException{
            loop=false;
            kafkaEmitor.shutdown();
        }
    }
}
