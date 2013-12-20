package org.flyingfortress.core;

import org.flyingfortress.exception.LifeCycleException;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 14/12/13
 * Time: 8:34 PM
 */
public interface LifeCycle {

    public void startup() throws LifeCycleException;
    public void shutdown() throws LifeCycleException;

}
