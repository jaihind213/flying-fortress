package org.flyingfortress.core.txn;

import org.flyingfortress.api.Destination;
import org.flyingfortress.api.Message;
import org.flyingfortress.core.Constants;
import org.flyingfortress.core.txn.buffer.MessageDataBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * User: vishnuhr
 * Date: 15/12/13
 * Time: 1:15 PM
 */
public class MessagePublisher {
    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    private static final ThreadLocal <MessageDataBuffer> bufferThreadLocal = new ThreadLocal <MessageDataBuffer> ();

    public static void addMessage(Message message,Destination destination) {
        TransactionState.STATE currentState = TransactionState.get();

        if(!currentState.equals(TransactionState.STATE.in_progress)){
            throw new IllegalStateException("Cant add message to transaction whose state is not in progress. currentState:"+currentState.toString());
        }
        if(message == null || destination ==null){
            throw new IllegalArgumentException("Message/Destination cant be null for publish call!");
        }
        checkAndCreate();
        bufferThreadLocal.get().addMessage(message, destination);
        TransactionState.set(TransactionState.STATE.in_progress);
        logger.info("added message to buffer! MessageId:"+message.getMsgId()+". ThreadId:"+Thread.currentThread().getId());
    }

    static void clear() {
        checkAndCreate();
        bufferThreadLocal.get().clear();
        logger.info("cleared message buffer! for threadId:"+Thread.currentThread().getId());
    }

    static MessageDataBuffer get(){
        checkAndCreate();
        return bufferThreadLocal.get();
    }

    public static int getCurrentBufferSize(){
        checkAndCreate();
        return bufferThreadLocal.get().size();
    }

    static void checkAndCreate(){
        MessageDataBuffer dataBuffer= bufferThreadLocal.get();
        if(dataBuffer== null){
            dataBuffer = new MessageDataBuffer();
            bufferThreadLocal.set(dataBuffer);
        }
    }


}
