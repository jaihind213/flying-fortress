package org.flyingfortress.exception.txn;

/**
 * Exception thrown if rollback fails.
 * User: vishnuhr
 * Date: 15/12/13
 * Time: 12:04 PM
 */
public class RollbackException extends TransactionException {

    public RollbackException(String txnId) {
        super(txnId,"Failed to rollback transaction.");
    }

    public RollbackException(String txnId,Throwable cause) {
        super(txnId,"Failed to rollback transaction.",cause);
    }

    public RollbackException(String txnId,String msg) {
        super(txnId,"Failed to rollback transaction."+msg);
    }

    public RollbackException(String txnId,String msg,Throwable cause) {
        super(txnId,"Failed to rollback transaction."+msg,cause);
    }


}
