package org.flyingfortress.api;

import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.KeyDeserializer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 19/12/13
 * Time: 11:50 AM
 */
public class DestinationKeyDeserializer extends KeyDeserializer
{
    public DestinationKeyDeserializer() {
    }

    @Override
    public Object deserializeKey( String key,
                                  DeserializationContext ctxt )
            throws IOException
    {
        return constructDestination(key);
    }

    /**
     * construction depends on toString method of Destination
     * @param key
     * @return Destination
     */
    private Destination constructDestination(String key){
        String name = key.substring(key.indexOf(":")+1, key.indexOf(","));
        String type = key.substring(key.lastIndexOf(":")+1 , key.lastIndexOf("}"));
        return new Destination(name, Destination.DESTINATION_TYPE.valueOf(type));
    }


}