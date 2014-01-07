package org.flyingfortress.test.core.emit;

import junit.framework.Assert;
import org.flyingfortress.api.Destination;
import org.flyingfortress.api.Message;
import org.flyingfortress.core.config.KafkaProducerConfig;
import org.flyingfortress.core.emit.KafkaEmitor;
import org.flyingfortress.exception.LifeCycleException;
import org.flyingfortress.test.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 31/12/13
 * Time: 3:58 PM
 */
public class KafkaEmitorTest extends BaseTest {

    @Before
    public void setUp() throws Exception {
        //todo: delete old Q and start afresh
    }

    @After
    public void tearDown() throws Exception {
        //todo: delete old Q and start afresh
    }

    private Destination destination = new Destination("KafkaEmitorTest", Destination.DESTINATION_TYPE.queue);

    @Test
    public void testEmition() throws Exception {
        KafkaEmitor kafkaEmitor= new KafkaEmitor(new KafkaProducerConfig(),"testKafkaEmitor:"+System.currentTimeMillis());
        kafkaEmitor.startup();
        kafkaEmitor.emit(new Message("pingTest"+System.currentTimeMillis()),destination);
    }

    @Test
    public void testEmitionWithIncorrectConfig() throws Exception {
        KafkaProducerConfig kpc = new KafkaProducerConfig(); kpc.setMetadata_broker_list("doesNotExist:9093");
        KafkaEmitor badEmitor= new KafkaEmitor(kpc,"testKafkaEmitor:"+System.currentTimeMillis());
        try {
            badEmitor.startup();
            Assert.fail("emitor started up inspight of bad config!");
        } catch (LifeCycleException testPassed) {   }
    }

    @Test
    public void testEmitionWithOutStartup() throws Exception {
        KafkaProducerConfig kpc = new KafkaProducerConfig();
        KafkaEmitor emitor= new KafkaEmitor(kpc,"testKafkaEmitor:"+System.currentTimeMillis());
        try {
            emitor.emit(new Message("pingTest"+System.currentTimeMillis()),destination);
            Assert.fail("emitor started up inspight of bad config!");
        } catch (IllegalStateException testPassed) {   }
    }

    @Test
    public void testShutdown() throws Exception {
        KafkaEmitor kafkaEmitor= new KafkaEmitor(new KafkaProducerConfig(),"testKafkaEmitor:"+System.currentTimeMillis());
        kafkaEmitor.startup();
        kafkaEmitor.shutdown();
    }

    @Test
    public void testEmitionAfterShutdown() throws Exception {
        KafkaEmitor kafkaEmitor= new KafkaEmitor(new KafkaProducerConfig(),"testKafkaEmitor:"+System.currentTimeMillis());
        kafkaEmitor.startup();
        kafkaEmitor.emit(new Message("pingTest"+System.currentTimeMillis()),destination);
        kafkaEmitor.shutdown();
        try {
            kafkaEmitor.emit(new Message("pingTest"+System.currentTimeMillis()),destination);
            Assert.fail("emitor sent out message up inspight of it being shutdown!");
        } catch (IllegalStateException testPassed) {}
    }
}
