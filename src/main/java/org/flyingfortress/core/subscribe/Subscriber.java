package org.flyingfortress.core.subscribe;

import org.flyingfortress.exception.subscribe.SubscriberException;

/**
 * User: vishnuhr
 * Date: 21/12/13
 * Time: 8:33 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Subscriber {

    public <M> void handleMessage(M message) throws SubscriberException;

    /**
     * register a subscriber to the subscriber implementing this interface.
     * Route object has a subscriber object
     * when subscriber object gets a message, it needs to give it to route object too (for transform)
     * @param subscriber subscriber interested in messages of this subsriber.
     * @throws IllegalArgumentException if subscriber is null.
     */
    public void registerCallback(Subscriber subscriber);

    /**
     * get subscriber ID
     * @return String
     */
    public String getId();

}
