package org.flyingfortress.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 21/12/13
 * Time: 5:09 PM
 */

public class IdGenerator {
    private  static AtomicLong atomic = new AtomicLong();
    public static final String def_prefix = "FF";   //default prefix

    public final static String seperator = ":";

    /**
     * generates a uuid using Atomic Long class of java taking a user specified prefix.
     * @param prefix  string. prefix 'msg' is used if prefix is empty/null
     * @return  uuid       example uuid 'prefix:1:1373265152683'     The third component is System.currentMillis()
     */
    public static String getAtomicID(String prefix){
        if(prefix ==null || prefix.isEmpty()){
            prefix = def_prefix;
        }
        return prefix+ seperator+ atomic.incrementAndGet()+seperator+System.currentTimeMillis() ;
    }

    /**
     * generates a uuid using UUID class of java taking a user specified prefix.
     * @param prefix  prefix string.prefix 'msg' is used if prefix is empty/null
     * @return  uuid   Example uuid is 'prefix:3adc6ceb-e8a8-4792-b73b-9b4524b3cf8f:1373265007538'  The third component is System.currentMillis()
     */
    public static String getUUID(String prefix){
        if((prefix == null) || prefix.isEmpty()){
            prefix = def_prefix;
        }
        return prefix+seperator+ UUID.randomUUID().toString()+seperator+System.currentTimeMillis();
    }

}
