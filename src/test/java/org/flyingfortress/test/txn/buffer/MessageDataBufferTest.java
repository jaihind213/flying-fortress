package org.flyingfortress.test.txn.buffer;

import org.flyingfortress.api.Destination;
import org.flyingfortress.api.Message;
import org.flyingfortress.core.txn.buffer.MessageDataBuffer;
import org.flyingfortress.util.JsonHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Set;

import static com.yammer.dropwizard.testing.JsonHelpers.jsonFixture;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 31/12/13
 * Time: 2:34 PM
 */
public class MessageDataBufferTest {

    private MessageDataBuffer messageDataBuffer = new MessageDataBuffer();
    private Destination destination1 = new Destination("d1", Destination.DESTINATION_TYPE.queue);
    private Destination destination2 = new Destination("d2", Destination.DESTINATION_TYPE.topic);

    @Before
    public void setUp() throws Exception {
        messageDataBuffer.clear();
        Message message1 = new Message("m1","m1",new HashMap<String, Object>());
        message1.setReceivedTime(123);
        Message message2 = new Message("m2","m2",new HashMap<String, Object>());
        message2.setReceivedTime(124);
        messageDataBuffer.addMessage(message1,destination1);
        messageDataBuffer.addMessage(message2, destination2);
        messageDataBuffer.addMessage(message2, destination1);
    }

    @Test
    public void serializesToJSON() throws Exception {
        Assert.assertEquals("a msgDataBuffer document can be serialized to JSON",
                JsonHelper.objectMapper.writeValueAsString(messageDataBuffer), ((jsonFixture("fixtures/msgDataBuffer.json"))));
    }

    @Test
    public void deSerializesFromJSON() throws Exception {
        MessageDataBuffer messageDataBuffer = JsonHelper.objectMapper.readValue(jsonFixture("fixtures/msgDataBuffer.json"),MessageDataBuffer.class);
        Set<Destination> destinationSet = messageDataBuffer.getDestinations();
        Assert.assertEquals(2,destinationSet.size());

        Assert.assertEquals(2,messageDataBuffer.getMessagesForDestination(destination1).size());
        Assert.assertEquals(1,messageDataBuffer.getMessagesForDestination(destination2).size());

        Assert.assertTrue(destinationSet.contains(destination1));
        Assert.assertTrue(destinationSet.contains(destination2));
        Assert.assertFalse(destinationSet.contains(new Destination("SomeRandomDestination", Destination.DESTINATION_TYPE.topic)));
    }

}
