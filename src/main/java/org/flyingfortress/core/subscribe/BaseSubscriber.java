package org.flyingfortress.core.subscribe;

import org.flyingfortress.api.Destination;
import org.flyingfortress.core.LifeCycle;
import org.flyingfortress.exception.LifeCycleException;
import org.flyingfortress.exception.subscribe.SubscriberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 21/12/13
 * Time: 8:35 AM
 */
public abstract class BaseSubscriber implements Subscriber,LifeCycle{

    protected static final Logger logger = LoggerFactory.getLogger(BaseSubscriber.class);

    protected final String id;
    /** The group to which this consumer belongs to.**/
    protected final String groupId;
    protected final Destination destination;

    protected boolean initialized = false;

    protected List<Subscriber> subscribers = new ArrayList<Subscriber>(); // to this Subscriber

    protected BaseSubscriber(String id,String groupId,Destination destination) {
        this.id = id;
        this.groupId = groupId;
        this.destination = destination;
        logger.info("Creating subscriber with ID:"+id+", groupId:"+groupId);
    }

    /**
     * handle a received message. The registered subscribers are informed here.
     * @param <M> message
     * @throws SubscriberException if unable to handle message.
     */
    public <M> void handleMessage(M message) throws SubscriberException {
        logger.info("Subscriber: " + id + ", received message, from destination:"+destination.getName());
        informRegisteredSubscribers(message);
    }

    /**
     * {@inheritDoc}
     */
    public void registerCallback(Subscriber subscriber) {
        if(subscriber == null){throw  new IllegalArgumentException("Cant register null subscriber for parent:"+this.getId());}
        logger.info("Parent Subscriber: " +id + " has registered subscriber:" + subscriber.getId()+", for destination:"+destination.getName());
        subscribers.add(subscriber);
    }

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    private <M> void informRegisteredSubscribers(M message){
        for(Subscriber s : subscribers){
            logger.info("Parent Subscriber: "+this.getId()+" handing over message to registered subscriber:"+s.getId());
            try {
                s.handleMessage(message);
            } catch (Throwable e) {
                logger.warn("Parent Subscriber: "+this.getId()+" failed to hand over message to registered subscriber:"+s.getId(),e);
            }
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void startup() throws LifeCycleException {

    }

    @Override
    public void shutdown() throws LifeCycleException {

    }
}
