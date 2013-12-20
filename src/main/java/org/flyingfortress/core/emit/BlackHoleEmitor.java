package org.flyingfortress.core.emit;

import org.flyingfortress.api.Destination;
import org.flyingfortress.api.Message;
import org.flyingfortress.exception.LifeCycleException;

/**
 * The emitorThreadLocal which emits into a blackhole.
 * Once a message goes into a blackhole, it can never be found.
 * User: vishnuhr
 * Date: 14/12/13
 * Time: 6:43 PM
 */
public class BlackHoleEmitor extends BaseEmitor{


    @Override
    /**
     * emits a message into a blackhole. Will never throw a emitorThreadLocal exception.
     */
    public void emit(Message message, Destination destination) {
        logger.info("Message emitted into blackhole. MessageBody:"+message.getBody());
        System.out.println("Your message has been emitted to a blackhole! Destination:"+destination.getName());
    }

    @Override
    public void startup() throws LifeCycleException {
        //do nothing
    }

    @Override
    public void shutdown() throws LifeCycleException {
        //do nothing
    }
}
