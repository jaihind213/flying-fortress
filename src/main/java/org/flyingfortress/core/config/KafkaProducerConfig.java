package org.flyingfortress.core.config;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 19/12/13
 * Time: 1:30 PM
 */
public class KafkaProducerConfig extends Configuration{

    public KafkaProducerConfig() {
        this.name = "kafkaProducerConfig" ;
        this.description="kafkaProducerConfig";
    }

    private String serializer_class = "kafka.serializer.StringEncoder";

    private String producerType="sync";

    private String metadata_broker_list =  "localhost:9092";

    private int request_required_acks = 1;

    private String partitioner_class =  "kafka.producer.DefaultPartitioner";

    private int no_of_producers=10;

    private float version = 0.8f;

    private String pingTopic = "FlyingFortress_Ping";

    public String getSerializer_class() {
        return serializer_class;
    }

    public void setSerializer_class(String serializer_class) {
        this.serializer_class = serializer_class;
    }

    public String getProducerType() {
        return producerType;
    }

    public void setProducerType(String producerType) {
        this.producerType = producerType;
    }

    public String getMetadata_broker_list() {
        return metadata_broker_list;
    }

    public void setMetadata_broker_list(String metadata_broker_list) {
        this.metadata_broker_list = metadata_broker_list;
    }

    public int getRequest_required_acks() {
        return request_required_acks;
    }

    public void setRequest_required_acks(int request_required_acks) {
        this.request_required_acks = request_required_acks;
    }

    public String getPartitioner_class() {
        return partitioner_class;
    }

    public void setPartitioner_class(String partitioner_class) {
        this.partitioner_class = partitioner_class;
    }

    public int getNo_of_producers() {
        return no_of_producers;
    }

    public void setNo_of_producers(int no_of_producers) {
        this.no_of_producers = no_of_producers;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getPingTopic() {
        return pingTopic;
    }

    public void setPingTopic(String pingTopic) {
        this.pingTopic = pingTopic;
    }

    public Properties getProducerConfigProperties(){

        Properties p = new Properties();
        p.put("serializer.class",this.getSerializer_class());
        p.put("metadata.broker.list",this.getMetadata_broker_list());
        p.put("request.required.acks",String.valueOf(this.getRequest_required_acks()));
        p.put("partitioner.class",this.getPartitioner_class());

        p.put("producer.type",this.getProducerType());

        return p;
    }
}