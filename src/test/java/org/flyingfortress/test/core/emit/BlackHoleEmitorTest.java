package org.flyingfortress.test.core.emit;

import org.flyingfortress.api.Destination;
import org.flyingfortress.api.Message;
import org.flyingfortress.core.emit.BlackHoleEmitor;
import org.flyingfortress.test.BaseTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 31/12/13
 * Time: 3:53 PM
 */
public class BlackHoleEmitorTest extends BaseTest {

    BlackHoleEmitor blackHoleEmitor = new BlackHoleEmitor();

    @Before
    public void setUp() throws Exception {
        blackHoleEmitor.startup();  //should never throw exception
    }

    @After
    public void tearDown() throws Exception {
        blackHoleEmitor.shutdown();   //should never throw exception
    }

    @Test
    public void testBlackHoleEmitorLifeCycle(){
        try {
            System.out.println("Testing blackhole emitor startup/shutdown in startup/teardown methods!");
            blackHoleEmitor.emit(new Message("Testing emition!"), new Destination("blackhole"));
        } catch (Throwable e) {
            e.printStackTrace();
            Assert.fail("blackhole emitor failed to emit.");
        }
    }
}
