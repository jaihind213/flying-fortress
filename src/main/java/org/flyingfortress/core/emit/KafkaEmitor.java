package org.flyingfortress.core.emit;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.codehaus.jackson.JsonProcessingException;
import org.flyingfortress.api.Destination;
import org.flyingfortress.api.Message;
import org.flyingfortress.core.config.KafkaProducerConfig;
import org.flyingfortress.exception.LifeCycleException;
import org.flyingfortress.exception.emit.EmitorException;
import org.flyingfortress.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 19/12/13
 * Time: 1:26 PM
 */
public class KafkaEmitor extends BaseEmitor{

    private static final Logger logger = LoggerFactory.getLogger(KafkaEmitor.class);
    private final String id;   //client id
    private KafkaProducerConfig kafkaConfig;
    private Producer<Integer,String> syncProducer;

    public static final String pingMsg= "ping_msg";

    public KafkaEmitor(KafkaProducerConfig config, String id) {
        this.kafkaConfig = config;
        this.id = "KakfaEmitor-"+id;
    }

    public KafkaEmitor(KafkaProducerConfig config ) {
        this(config,"KakfaEmitor-"+System.currentTimeMillis());
    }


    @Override
    public void emit(Message message, Destination destination) throws EmitorException {
        if(!initialized){
            throw new IllegalStateException("Cant emit message as this emitor has not been initialized! startup not called!");
        }
        try {
            logger.debug("KafkaEmitor Emiting msgBody:"+ JsonHelper.objectMapper.writeValueAsString(message)+", msgId:"+message.getMsgId());
        } catch (IOException warning) {
            logger.warn("KafkaEmitor Emiting msg with id::" + message.getMsgId() + ".could not log body as:"+warning.getMessage());
        }

        try {
            KeyedMessage<Integer, String> data = new KeyedMessage<Integer, String>(destination.getName(), JsonHelper.objectMapper.writeValueAsString(message));
            long start_time= System.currentTimeMillis();
            syncProducer.send(data);
            long end_time= System.currentTimeMillis();
            logger.info("[TIME_TAKEN_EMITTION] Time taken to emit(ms):"+(end_time-start_time));
        } catch (Throwable e) {
            logger.error("Failed to send message to kafka msgId:"+message.getMsgId(),e);
            throw new EmitorException("Failed to send message to kafka."+"msgID:"+message.getMsgId(), e);

        }
        logger.info("KafkaEmitor Emited! msgID:"+message.getMsgId());
    }

    @Override
    public void startup() throws LifeCycleException {
        super.startup();
        try {
            this.syncProducer = new Producer<Integer, String>(new ProducerConfig(kafkaConfig.getProducerConfigProperties()));
            KeyedMessage<Integer, String> data = new KeyedMessage<Integer, String>(kafkaConfig.getPingTopic(), new String(pingMsg+":bombs away!: "+ new Date()));
            syncProducer.send(data);
            logger.info("Initialized kafka emitor resources! Id:"+id);
            initialized=true;
        } catch (Throwable e) {
            throw new LifeCycleException("Unable to Initialize Kafka emitor!",e);
        }
    }

    @Override
    public void shutdown() throws LifeCycleException {
        super.shutdown();
        try {
            if(initialized){
                this.syncProducer.close();
            }
        } catch (Exception e) {
            logger.warn("tried to shutdown kafka emitor resources! Id:"+id,e);
        }
        syncProducer= null; //release reference.
        this.initialized=false;
        logger.info("Shutdown kafka emitor resources! Id:"+id);
    }
}
