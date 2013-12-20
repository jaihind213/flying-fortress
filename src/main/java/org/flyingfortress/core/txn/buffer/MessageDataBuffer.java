package org.flyingfortress.core.txn.buffer;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.flyingfortress.api.Destination;
import org.flyingfortress.api.DestinationKeyDeserializer;
import org.flyingfortress.api.Message;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 19/12/13
 * Time: 9:52 AM
 */
public class MessageDataBuffer implements BufferOperations{
    @JsonProperty
    @JsonDeserialize( keyUsing = DestinationKeyDeserializer.class )
    private Map<Destination,List<Message>> data = new HashMap<Destination, List<Message>>();

    @Override
    public void clear() {
         data.clear();
    }

    @Override
    public void sync() throws IOException {
        //do nothing. for now.
    }

    public void addMessage(Message msg , Destination destination){
        List <Message>msgList = data.get(destination);
        if(msgList == null){
            msgList = new ArrayList<Message>();
            data.put(destination,msgList);
        }
        msgList.add(msg);
    }

    /**
     * returns message for a destination.
     * @param destination
     * @return list of messages. empty list if nothing there.
     */
    public List<Message> getMessagesForDestination(Destination destination) {
        List<Message> msgList = data.get(destination);
        if (msgList == null) {
            msgList = new ArrayList<Message>();
        }
        return msgList;
    }

    public int size(){
        return data.size();
    }
    @JsonIgnore
    public Set<Destination>  getDestinations(){
        return data.keySet();
    }
}
