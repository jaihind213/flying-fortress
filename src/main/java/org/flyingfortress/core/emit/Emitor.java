package org.flyingfortress.core.emit;

import org.flyingfortress.api.Destination;
import org.flyingfortress.api.Message;
import org.flyingfortress.core.LifeCycle;
import org.flyingfortress.exception.emit.EmitorException;

/**
 * User: vishnuhr
 * Date: 14/12/13
 * Time: 6:11 PM
 */
public interface Emitor extends LifeCycle{
    /**
     *
     * @param message
     * @param destination
     * @throws EmitorException if error occurs in emition.
     * @throws IllegalStateException if emitor not initialized i.e. startup method was not called. refer  <code>LifeCycle interface<code/>
     */
    public void emit(Message message, Destination destination)throws EmitorException;

}
