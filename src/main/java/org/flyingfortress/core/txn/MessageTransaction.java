package org.flyingfortress.core.txn;

import org.flyingfortress.api.Destination;
import org.flyingfortress.api.Message;
import org.flyingfortress.core.TransactionManager;
import org.flyingfortress.core.config.TransactionConfiguration;
import org.flyingfortress.core.emit.Emitor;
import org.flyingfortress.core.txn.buffer.MessageDataBuffer;
import org.flyingfortress.exception.txn.CommitException;
import org.flyingfortress.exception.txn.RollbackException;
import org.flyingfortress.exception.txn.TransactionException;
import org.flyingfortress.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 15/12/13
 * Time: 6:38 AM
 */
public class MessageTransaction {
    private static final Logger logger = LoggerFactory.getLogger(MessageTransaction.class);
    public static final ThreadLocal <String> txnIdThreadLocal = new ThreadLocal<String>();

    /**
     * Start a transaction
     * @throws IllegalStateException if transaction already active.
     * @throws org.flyingfortress.exception.txn.TransactionException for other errors.
     */
    public static void start() throws TransactionException {
        //set boolean for thread that transaction has started or throw exception
        TransactionState.STATE currentState = TransactionState.get();
        if(!currentState.equals(TransactionState.STATE.not_started)){
            throw new IllegalStateException("Cant start a transaction whose state is:"+currentState.toString());
        }

        String txId = "TxnId:"+Thread.currentThread().getId()+"::"+System.currentTimeMillis();
        txnIdThreadLocal.set(txId);//todo. id generator.
        TransactionState.set(TransactionState.STATE.in_progress);
        logger.info("Transaction started with Id:"+txId);
    }

    /**
     * rollback a transaction
     */
    public static void rollback()throws RollbackException {
        //do your stuff...reset
        String txnId =  txnIdThreadLocal.get();
        logger.info("Transaction rolling back for txnId:" + txnId);
        resetState();
        logger.info("Transaction rollback success! txnId:" + txnId);
    }

    /**
     *  commit a transaction
     */
    public static void commit() throws CommitException {
        //do your stuff...reset
        MessageDataBuffer buffer = MessagePublisher.get();
        String txnId= txnIdThreadLocal.get();
        try {
            if(buffer.size() == 0){
                logger.info("Committing buffer with 0 messages! nothing to do");
            }else{
                Message bulkMessage = prepareBulkMessage(buffer);
                Emitor emitor = TransactionManager.getInstance().getEmitor();
                Set<Destination> destinations = new HashSet<Destination>();
                for(Destination d: buffer.getDestinations()) {
                    destinations.add(d);
                }
                emitor.emit(bulkMessage,TransactionManager.getInstance().decideDestination(destinations));
            }
            logger.info("Transaction commit success! txnId:" + txnId);
        } catch (Exception e) {
            logger.error("Failed to commit txn:"+ txnId,e);
            try {
                rollback();
            } catch (RollbackException re) {
                logger.warn("Failed to rollback after commit failed!",re);
            }
            throw new CommitException(txnId,e);
        } finally {
            resetState();
        }
    }

    private static void resetState(){
        if(TransactionState.get() != TransactionState.STATE.not_started)
        {
            logger.info("Transaction resources being reset! txnId:" + txnIdThreadLocal.get());
            TransactionState.set(TransactionState.STATE.not_started);
            MessagePublisher.clear();
            String txnId= txnIdThreadLocal.get();
            txnIdThreadLocal.set(null);
            logger.info("Transaction resources reset! txnId:" + txnId);
        }
    }

    private static Message prepareBulkMessage(MessageDataBuffer buffer) throws IOException {
        String body = JsonHelper.objectMapper.writeValueAsString(buffer);
        return new Message(txnIdThreadLocal.get(),body);
    }
}

