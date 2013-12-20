package org.flyingfortress.exception;

/**
 * Exceptions thrown during a lifecycle of an entity.
 * User: vishnuhr
 * Date: 14/12/13
 * Time: 8:34 PM
 */
public class LifeCycleException extends Exception{

    public LifeCycleException(String message) {
        super(message);
    }

    public LifeCycleException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
