package org.flyingfortress.api;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 14/12/13
 * Time: 6:13 PM
 */
public class Message <M> {

    private final String msgId;         //todo : id generator later on

    /**
     * The message belongs to a stream having this ID.
     */
    private final M body;

    /**
     * Ideally the time of creation of this messsage. or time message was received in this system.
     * Use the setter method if you wish to set this time, but use wisely
     */
    private long receivedTime;


    private Map<String,Object> headers = new HashMap<String,Object>();

    @JsonCreator
    public Message(@JsonProperty("msgId") String msgId, @JsonProperty("body") M body,  @JsonProperty("headers") Map <String,Object>headers) {
        this.msgId = msgId;
        this.body = body;
        this.receivedTime= System.currentTimeMillis();
        this.headers = headers;
    }
    //@JsonCreator
    public Message(  M body, Map <String,Object>headers) {
        this("msgId"+String.valueOf(System.currentTimeMillis()),body,headers);
    }

    //@JsonCreator
    public Message( M body) {
        this("msgId"+String.valueOf(System.currentTimeMillis()),body,new HashMap<String, Object>());
    }
    //@JsonCreator
    public Message(String msgId, M body) {
        this(msgId,body,new HashMap<String, Object>());
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public String getMsgId() {
        return msgId;
    }

    public M getBody() {
        return body;
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper  = new ObjectMapper();
        Message m = new Message("1","body");
        String json = objectMapper.writeValueAsString(m);
        System.out.println(json);
        Message m2 = objectMapper.readValue(json,Message.class);
        int a =1;
    }
}
