package org.flyingfortress.core.subscribe;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import org.flyingfortress.api.Destination;
import org.flyingfortress.core.config.KafkaConsumerConfig;
import org.flyingfortress.exception.LifeCycleException;
import org.flyingfortress.exception.subscribe.SubscriberException;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * User: vishnuhr
 * Date: 21/12/13
 * Time: 8:57 AM
 * //todo: handle connection loss and restart
 */
public class SimpleKafkaSubscriber extends BaseSubscriber implements Runnable{

    private final KafkaConsumerConfig config;
    private CountDownLatch startupLatch;
    private ConsumerConnector consumer;
    private String topic;
    protected long sleepTime = 5; //in ms  //this is probably bad.

    public SimpleKafkaSubscriber(String id, String groupID,Destination destination ,KafkaConsumerConfig config) {
        super(id, groupID, destination);
        this.topic=destination.getName();
        this.config = config;
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
        this.startupLatch = new CountDownLatch(1);
    }

    @Override
    public void startup() throws LifeCycleException {
        this.initialized=true;
        logger.info("Starting up subscriber... id:"+id+", groupId:"+groupId+", destination:"+destination.getName());
        Thread subscriberThread = new Thread(this);
        subscriberThread.setDaemon(false);
        subscriberThread.start();
        try {
            startupLatch.await();
        } catch (InterruptedException wontHappen) {
        }
        logger.info("Started up subscriber... id:"+id+", groupId:"+groupId+", destination:"+destination.getName());
    }

    @Override
    public void shutdown() throws LifeCycleException {
        try {
            this.initialized=false;//setting boolean before - conscious decision !
            consumer.shutdown();
            logger.info("shutdown subscriber... id:"+id+", groupId:"+groupId+", destination:"+destination.getName());
        } catch (Exception e) {
            logger.warn("Error on shutdown of subscriber:"+id+". groupId:"+groupId+",for destination:"+destination.getName(),e);
        }
    }

    @Override
    public void run() {    //seems bad to have run as startup method is there !
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream =  consumerMap.get(topic).get(0);

        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        startupLatch.countDown();

        while(it.hasNext()) {
            try {Thread.sleep(sleepTime);} catch (InterruptedException ignore) {}
            String msg= new String(it.next().message());
            try {
                handleMessage(msg);
            } catch (SubscriberException e) {
                logger.error("Failed to handle message. Id:"+id+", groupdId:"+groupId+",Destination:"+destination.getName()+".Message:"+msg,e);
            }
        }
    }

    @Override
    public <M> void handleMessage(M message) throws SubscriberException {
        System.out.println((String)message);
        super.handleMessage(message);
    }

    private ConsumerConfig createConsumerConfig()
    {
        Properties props = new Properties();
        props.put("zookeeper.connect", config.getZkConnect());

        if(id != null && !id.isEmpty()){
            props.put("consumer.id", id);
            props.put("group.id", groupId == null || groupId.isEmpty() ? id: groupId);
            logger.info("Setting groupId as "+id+", as groupId specified was null/empty");
        }

        props.put("zookeeper.session.timeout.ms", String.valueOf(config.getZkSessionTimeout()));
        props.put("zookeeper.sync.time.ms", String.valueOf(config.getZkSyncTimeOut()));
        props.put("auto.commit.interval.ms", String.valueOf(config.getAutoCommitInterval()));
        props.put("auto.commit.enable", String.valueOf(config.isAutoCommitEnable()));

        return new ConsumerConfig(props);

    }
}
