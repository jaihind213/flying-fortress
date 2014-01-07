package org.flyingfortress.core.txn;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
import org.flyingfortress.api.Destination;
import org.flyingfortress.api.Message;
import org.flyingfortress.core.Constants;
import org.flyingfortress.core.config.KafkaConsumerConfig;
import org.flyingfortress.core.subscribe.BaseSubscriber;
import org.flyingfortress.core.subscribe.SimpleKafkaSubscriber;
import org.flyingfortress.core.subscribe.Subscriber;
import org.flyingfortress.core.txn.buffer.MessageDataBuffer;
import org.flyingfortress.exception.LifeCycleException;
import org.flyingfortress.exception.subscribe.SubscriberException;
import org.flyingfortress.util.JsonHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: vishnuhr
 * Date: 21/12/13
 * Time: 4:23 PM
 */
public class MessageSubscriber extends BaseSubscriber {

    private final KafkaConsumerConfig config;
    private List<SimpleKafkaSubscriber> txnTopicSubcribers = new ArrayList<SimpleKafkaSubscriber>();

    private final String nativeTopicIdPrefix="_ff_native_";
    private final String txnGroupPrefix="_ff_grp_txn_";
    private final String zkTopicResourcePath = "/brokers/topics";

    public MessageSubscriber(String id, String groupId, Destination destination,KafkaConsumerConfig config) {
        super(id, groupId, destination);
        this.config= config;
    }

    @Override
    public void startup() throws LifeCycleException {
       super.startup();
       String subIdForNativeTopic = nativeTopicIdPrefix+id;
       SimpleKafkaSubscriber originalTopicSubscriber = new SimpleKafkaSubscriber(subIdForNativeTopic,groupId,destination,config);
       txnTopicSubcribers.add(originalTopicSubscriber);
       originalTopicSubscriber.registerCallback(this);
       try
       {
            List<String> topics = getTransactionTopics();
            for(String topic : topics)
            {
                Destination txnDestination = new Destination(topic);
                SimpleKafkaSubscriber sks = new SimpleKafkaSubscriber( txnGroupPrefix+id, txnGroupPrefix+groupId, txnDestination,config );
                sks.registerCallback(this);
                txnTopicSubcribers.add(sks);
            }
        } catch (Exception e) {
            throw new LifeCycleException("failed to get topics!",e);
        }
        for(SimpleKafkaSubscriber subscriber : txnTopicSubcribers){
            subscriber.startup();
        }
        logger.info("Message subscriber started up for destination:"+destination.getName()+". id:"+id+",groupId:"+groupId);
    }

    @Override
    public void shutdown() throws LifeCycleException {
        super.shutdown();
        for(SimpleKafkaSubscriber s: txnTopicSubcribers){
            s.shutdown();
        }
    }

    @Override
    public <M> void handleMessage(M message) throws SubscriberException {
        {
            for(Subscriber s : subscribers){
                try {
                    List<Message> messages = processMessage(message);
                    logger.info("TXN-Message-Subscriber: "+this.getId()+" handing over message to registered subscriber:"+s.getId());
                    for(Message m: messages){
                        s.handleMessage(m);
                    }
                } catch (Throwable e) {
                    logger.warn("Parent Subscriber: "+this.getId()+" failed to hand over message to registered subscriber:"+s.getId(),e);
                }
            }
        }
    }

    private <M> List<Message> processMessage(M message)throws  SubscriberException{
        List<Message> messages = new ArrayList<Message>();
        try {
            Message m = JsonHelper.objectMapper.readValue((String)message,Message.class);
            MessageDataBuffer buffer= JsonHelper.objectMapper.readValue((String)m.getBody(),MessageDataBuffer.class);
            messages = buffer.getMessagesForDestination(destination);
            buffer.clear();
        } catch (IOException e) {
            throw new SubscriberException(e);
        }
        return messages;
    }

    private List<String> getTransactionTopics()throws Exception{
        List <String> topics = new ArrayList<String>();
        ZkClient zkClient = new ZkClient(config.getZkConnect(), 4000, 6000, new BytesPushThroughSerializer());//todo dont hard code the timeouts here...
        try {
            List<String> topicList = zkClient.getChildren(zkTopicResourcePath);
            for(String topicName: topicList)
            {
                if(topicName.startsWith(Constants.FLYING_FORTRESS_TOPIC_PREFIX)
                    && TransactionManager.getInstance().isDestinationInGroup(destination.getName(),topicName))
                {
                    topics.add(topicName);
                }
            }
        }catch(Throwable e){
            throw new Exception("Failed to get Topics list from zookeeper!",e); //getChildren can throw exceptions , propogate them
        }finally {
            try { zkClient.close();} catch (ZkInterruptedException e) { logger.warn("Error in closing zk client while getting list of topics.",e); }
        }
        return topics;
    }
}
