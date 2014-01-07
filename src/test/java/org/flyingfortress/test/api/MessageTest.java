package org.flyingfortress.test.api;

import junit.framework.Assert;
import org.flyingfortress.api.Message;
import org.flyingfortress.test.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import static com.yammer.dropwizard.testing.JsonHelpers.asJson;
import static com.yammer.dropwizard.testing.JsonHelpers.jsonFixture;


/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 24/12/13
 * Time: 8:03 PM
 */
public class MessageTest extends BaseTest {

    @Test
    public void serializesToJSON() throws Exception {
        Map<String,Object> headers = new HashMap<String, Object>();
        headers.put("headerName","headerValue") ;
        final Message document = new Message("id","body",headers);
        document.setReceivedTime(123);
        Assert.assertEquals("a Message document can be serialized to JSON",
                asJson(document),((jsonFixture("fixtures/msg.json"))));
    }

}
