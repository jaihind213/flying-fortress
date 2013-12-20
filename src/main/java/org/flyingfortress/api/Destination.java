package org.flyingfortress.api;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Destination where a message should be delivered
 * User: vishnuhr
 * Date: 14/12/13
 * Time: 6:26 PM
 */
public class Destination {

    public enum DESTINATION_TYPE{
        topic, queue
    }
    static final String nameKey = "DestinationName";
    static final String typeKey = "DestinationType";


    private String name;

    private DESTINATION_TYPE type = DESTINATION_TYPE.queue;

    @JsonCreator
    public Destination(@JsonProperty("name") String name,@JsonProperty("type") DESTINATION_TYPE type) {
        if(name == null || name.isEmpty()){throw new IllegalArgumentException("Cannot create destination whose name is null/empty!");}
        this.name = name;
        this.type = type;
    }

    public Destination(String name) {
        this(name,DESTINATION_TYPE.queue);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DESTINATION_TYPE getType() {
        return type;
    }

    public void setType(DESTINATION_TYPE type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{"+nameKey+":"+name+","+typeKey+":"+type+"}";
    }

    @Override
    public int hashCode() {
       return toString().hashCode();
    }

    @Override

    public boolean equals(Object o) {
        boolean result = true;
        if(o == null || !(o instanceof Destination) ){
            result = false;
        }
        Destination other = (Destination)o;
        if(!(this.getName().equalsIgnoreCase(other.getName()) && this.getType() == (other.getType()))){
            result = false;
        }
        return result;

    }
}
