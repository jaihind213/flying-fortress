package org.flyingfortress.core.config;

import org.codehaus.jackson.annotate.JsonProperty;
import org.flyingfortress.api.EMITOR_TYPE;
import org.flyingfortress.api.TRANSACTION_LEVEL;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 15/12/13
 * Time: 5:00 PM
 */
public class TransactionConfiguration  extends Configuration{
    @JsonProperty
    private TRANSACTION_LEVEL level  = TRANSACTION_LEVEL.serializable;
    @JsonProperty
    private EMITOR_TYPE type = EMITOR_TYPE.kafka;
    @JsonProperty
    private int topicNameMaxLength = 32;

    @JsonProperty
    private KafkaProducerConfig kafkaProducerConfig;

    @JsonProperty
    private KafkaConsumerConfig kafkaConsumerConfig;

    public TransactionConfiguration() {
        this.name = "txnMgr";
    }

    public TRANSACTION_LEVEL getLevel() {
        return level;
    }

    public void setLevel(TRANSACTION_LEVEL level) {
        this.level = level;
    }

    public EMITOR_TYPE getType() {
        return type;
    }

    public void setType(EMITOR_TYPE type) {
        this.type = type;
    }

    public int getTopicNameMaxLength() {
        return topicNameMaxLength;
    }

    public void setTopicNameMaxLength(int topicNameMaxLength) {
        this.topicNameMaxLength = topicNameMaxLength;
    }

    public KafkaProducerConfig getKafkaProducerConfig() {
        return kafkaProducerConfig;
    }

    public void setKafkaProducerConfig(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaProducerConfig = kafkaProducerConfig;
    }

    public KafkaConsumerConfig getKafkaConsumerConfig() {
        return kafkaConsumerConfig;
    }

    public void setKafkaConsumerConfig(KafkaConsumerConfig kafkaConsumerConfig) {
        this.kafkaConsumerConfig = kafkaConsumerConfig;
    }
}
