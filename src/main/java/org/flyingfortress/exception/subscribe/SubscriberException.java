package org.flyingfortress.exception.subscribe;

/**
 * IF Subscriber unable to handle messsage.
 * User: vishnuhr
 * Date: 21/12/13
 * Time: 8:34 AM
 */
public class SubscriberException extends Exception{
    public SubscriberException() {
        super("Internal error in Subscriber!");
    }

    public SubscriberException(String s) {
        super(s);
    }

    public SubscriberException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SubscriberException(Throwable throwable) {
        super("Internal error in Subscriber!",throwable);
    }
}
