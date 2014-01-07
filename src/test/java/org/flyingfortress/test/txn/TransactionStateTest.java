package org.flyingfortress.test.txn;

import org.flyingfortress.api.Destination;
import org.flyingfortress.api.EMITOR_TYPE;
import org.flyingfortress.api.Message;
import org.flyingfortress.core.config.TransactionConfiguration;
import org.flyingfortress.core.txn.MessagePublisher;
import org.flyingfortress.core.txn.MessageTransaction;
import org.flyingfortress.core.txn.TransactionManager;
import org.flyingfortress.core.txn.TransactionState;
import org.flyingfortress.test.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 31/12/13
 * Time: 5:49 PM
 */
public class TransactionStateTest extends BaseTest {

    TransactionConfiguration configuration ;

    @Before
    public void setUp() throws Exception {
        configuration = new TransactionConfiguration();
        configuration.setType(EMITOR_TYPE.blackhole);
        TransactionManager.init(configuration);
        try {
            //do reset.
            MessageTransaction.rollback();
        } catch (IllegalStateException ingore) {
        }
    }

    @Test
    public void testTransactionStateBeforeStarting() throws Exception {
        Assert.assertEquals(TransactionState.STATE.not_started,MessageTransaction.getState());
    }

    @Test
    public void testSimpleTransaction() throws Exception {
        MessageTransaction.start();
        Assert.assertEquals(TransactionState.STATE.in_progress,MessageTransaction.getState());
        MessagePublisher.addMessage(new Message("testSimpleTransaction"),new Destination("testSimpleTransaction"));
        Assert.assertNotNull(MessageTransaction.getId());
        MessageTransaction.commit();
        Assert.assertEquals(TransactionState.STATE.not_started,MessageTransaction.getState());
    }

    @Test
    public void testSimpleRollbackTransaction() throws Exception {
        MessageTransaction.start();
        MessagePublisher.addMessage(new Message("testSimpleTransaction"),new Destination("testSimpleTransaction"));
        MessageTransaction.rollback();
        Assert.assertEquals(TransactionState.STATE.not_started,MessageTransaction.getState());
        Assert.assertNull(MessageTransaction.getId());
    }

    @Test
    public void testDuplicateStart() throws Exception {
        MessageTransaction.start();
        MessagePublisher.addMessage(new Message("testSimpleTransaction"), new Destination("testSimpleTransaction"));
        try {
            MessageTransaction.start();
            Assert.fail("test failed. one cant start an allready active txn!");
        } catch (IllegalStateException testPassed) {
            //testPassed.printStackTrace();
        }
    }

    @Test
    public void testRollbackBeforeStarting() throws Exception {
        try {
            MessageTransaction.rollback();
            Assert.fail("test failed. one cant rollback txn which is not in progress!");
        } catch (IllegalStateException testPassed) {
            //testPassed.printStackTrace();
        }
    }

}
