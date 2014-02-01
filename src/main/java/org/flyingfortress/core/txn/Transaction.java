package org.flyingfortress.core.txn;

import org.flyingfortress.exception.txn.CommitException;
import org.flyingfortress.exception.txn.RollbackException;
import org.flyingfortress.exception.txn.TransactionException;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 07/01/14
 * Time: 8:58 AM
 */
public  interface Transaction {

    public void commit() throws CommitException;

    public void rollback()throws RollbackException;

    public void start() throws TransactionException;
}
