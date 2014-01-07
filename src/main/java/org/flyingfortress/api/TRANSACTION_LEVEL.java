package org.flyingfortress.api;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 15/12/13
 * Time: 5:05 PM
 */
public enum TRANSACTION_LEVEL {
    /** kafka transactions to take place in serial order*/
    serializable,
    /** kafka transactions to take place in parallel*/
    parallel
}
