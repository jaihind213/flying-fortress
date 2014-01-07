package org.flyingfortress.core.txn;

/**
 * User: vishnuhr
 * Date: 15/12/13
 * Time: 6:43 AM
 */
public class TransactionState {

    public enum STATE{
        not_started, in_progress, complete
    }
    private static final ThreadLocal txnState = new ThreadLocal();

    public static void set(STATE state) {
        txnState.set(state);
    }

    public static void unset() {
        txnState.remove();
    }

    public static STATE get()  {
        if(txnState.get() == null){
            set(STATE.not_started);
        }
        return (STATE)txnState.get();
    }
}