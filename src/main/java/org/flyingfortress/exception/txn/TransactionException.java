package org.flyingfortress.exception.txn;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 15/12/13
 * Time: 12:03 PM
 */
public class TransactionException extends  Exception {

    private final String txnId;

    public TransactionException(String txnId, String msg) {
        super("Error in transaction:"+txnId+"."+msg);
        this.txnId = txnId;
    }

    public TransactionException(String txnId, String msg, Throwable cause) {
        super("Error in transaction:"+txnId+"."+msg,cause);
        this.txnId = txnId;
    }

    public String getTxnId() {
        return txnId;
    }
}
