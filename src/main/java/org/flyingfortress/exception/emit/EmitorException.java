package org.flyingfortress.exception.emit;

/**
 * An exception thrown when emitorThreadLocal fails to emit.
 * User: vishnuhr
 * Date: 14/12/13
 * Time: 6:31 PM
 */
public class EmitorException extends Exception{

    public EmitorException(String message) {
        super(message);
    }

    public EmitorException(String message, Throwable cause) {
        super(message, cause);
    }
}
