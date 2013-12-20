package org.flyingfortress.exception.emit;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 15/12/13
 * Time: 5:53 PM
 */
public class EmitorInstantiationException extends Exception{

    public EmitorInstantiationException(String s) {
        super(s);
    }

    public EmitorInstantiationException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
