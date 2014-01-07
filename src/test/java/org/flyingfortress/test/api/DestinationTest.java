package org.flyingfortress.test.api;

import org.flyingfortress.api.Destination;
import org.flyingfortress.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import static com.yammer.dropwizard.testing.JsonHelpers.asJson;
import static com.yammer.dropwizard.testing.JsonHelpers.jsonFixture;


import static com.yammer.dropwizard.testing.JsonHelpers.asJson;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 31/12/13
 * Time: 2:28 PM
 */
public class DestinationTest extends BaseTest {

    @Test
    public void serializesToJSON() throws Exception {
        Destination destination = new Destination("testDestination", Destination.DESTINATION_TYPE.queue);
        Assert.assertEquals("a destination document can be serialized to JSON",
                asJson(destination), ((jsonFixture("fixtures/destination.json"))));
    }
}
