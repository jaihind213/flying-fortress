package org.flyingfortress.exception.txn;

/**
 * Exception thrown if commit fails.
 * User: vishnuhr
 * Date: 15/12/13
 * Time: 12:05 PM
 */
public class CommitException extends TransactionException {

    private final String txnId ;

    public CommitException(String txnId) {
        super(txnId,"Failed to commit transaction");
        this.txnId = txnId;
    }
    public CommitException(String txnId,Throwable cause) {
        super(txnId,"Failed to commit transaction",cause);
        this.txnId = txnId;
    }

    public CommitException(String txnId, String msg) {
        super(txnId,msg);
        this.txnId = txnId;
    }

    public CommitException(String txnId, String msg, Throwable cause) {
        super(txnId,msg, cause);
        this.txnId = txnId;
    }
}
