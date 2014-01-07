package org.flyingfortress.core.emit;

import org.flyingfortress.exception.LifeCycleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: vishnuhr
 * Date: 14/12/13
 * Time: 6:44 PM
 */
public abstract class BaseEmitor implements Emitor{
    protected static final Logger logger = LoggerFactory.getLogger(BaseEmitor.class);
    protected boolean initialized = false;

    public void startup() throws LifeCycleException {
        //do nothing
    }

    public void shutdown() throws LifeCycleException {
        //do nothing
    }

    public boolean isInitialized(){
        return initialized;
    }
}
