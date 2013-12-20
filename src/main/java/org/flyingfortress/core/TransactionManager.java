package org.flyingfortress.core;

import org.flyingfortress.api.Destination;
import org.flyingfortress.core.config.TransactionConfiguration;
import org.flyingfortress.core.emit.BlackHoleEmitor;
import org.flyingfortress.core.emit.Emitor;
import org.flyingfortress.core.emit.KafkaEmitor;
import org.flyingfortress.exception.emit.EmitorInstantiationException;
import org.flyingfortress.exception.LifeCycleException;
import org.flyingfortress.util.RaddixSort;

import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 15/12/13
 * Time: 4:36 PM
 */
public class TransactionManager{

    private TransactionConfiguration configuration;
    private Emitor emitorResource;//used only when TRANSACTION_LEVEL.serializable

    private static TransactionManager transactionManager;  //singleton

    public static void init(TransactionConfiguration configuration) {
        if(transactionManager == null){
            transactionManager = new TransactionManager(configuration);
        }
    }

    public static TransactionManager getInstance() {
        if(transactionManager == null ){throw new IllegalStateException("TransactionManager has not been initialized!");}
        return transactionManager;
    }

    private TransactionManager(TransactionConfiguration configuration) {
        this.configuration=configuration;
    }
    public Emitor getEmitor() throws EmitorInstantiationException {
        return transactionManager.createEmitor();
    }

    public Destination decideDestination(Set<Destination> destinations){
        String [] destNameArray = new String[destinations.size()];
        Iterator<Destination> iterator  = destinations.iterator();

        int i = 0;
        while (iterator.hasNext()){
            destNameArray[i++] = iterator.next().getName();
        }

        RaddixSort.radixSort(destNameArray,transactionManager.configuration.getTopicNameMaxLength());
        Destination destinationForTxn = new Destination(prepareDestinationName(destNameArray));
        return destinationForTxn;
    }

    private String prepareDestinationName(String []destArray){
           StringBuffer temp = new StringBuffer(destArray.length);
           for(int i = 0; i< destArray.length;i++){
               temp.append(destArray[i]);
           }
        return temp.toString();
    }
    private Emitor createEmitor() throws EmitorInstantiationException {
        Emitor toReturn = null;

        switch (configuration.getLevel())
        {
            case serializable:
                if (emitorResource == null) {
                    switch (configuration.getType()) {
                        case blackhole:
                            emitorResource = new BlackHoleEmitor();
                            try {
                                emitorResource.startup();
                            } catch (LifeCycleException willNeverHappen) {  }
                            break;
                        case kafka:
                            emitorResource = new KafkaEmitor(this.configuration.getKafkaProducerConfig());
                            try {
                                emitorResource.startup();
                            } catch (LifeCycleException e) {
                              throw new EmitorInstantiationException("failed to create kafka emitor.",e);
                            }
                            break;
                    }
                }
                toReturn = emitorResource;
                break;
            case parallel:
                break;
        }

        return toReturn;
    }

}
