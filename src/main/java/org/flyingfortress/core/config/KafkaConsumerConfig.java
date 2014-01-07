package org.flyingfortress.core.config;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 21/12/13
 * Time: 9:09 AM
 */
public class KafkaConsumerConfig extends Configuration{

    public KafkaConsumerConfig() {
        this.name="kafka simple consumer config";
        this.description="kafka simple consumer config which saves offset to zookeeper"   ;
    }

    private String zkConnect = "localhost:2181";

    private String autoOffsetReset = "largest";

    private long zkSessionTimeout =400;

    private long zkSyncTimeOut =200;

    private long autoCommitInterval = 1000;

    private int fetchMaxBytes = 1024 * 1024;

    private boolean autoCommitEnable = false;

    public String getZkConnect() {
        return zkConnect;
    }

    public void setZkConnect(String zkConnect) {
        this.zkConnect = zkConnect;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public long getZkSessionTimeout() {
        return zkSessionTimeout;
    }

    public void setZkSessionTimeout(long zkSessionTimeout) {
        this.zkSessionTimeout = zkSessionTimeout;
    }

    public long getZkSyncTimeOut() {
        return zkSyncTimeOut;
    }

    public void setZkSyncTimeOut(long zkSyncTimeOut) {
        this.zkSyncTimeOut = zkSyncTimeOut;
    }

    public long getAutoCommitInterval() {
        return autoCommitInterval;
    }

    public void setAutoCommitInterval(long autoCommitInterval) {
        this.autoCommitInterval = autoCommitInterval;
    }

    public int getFetchMaxBytes() {
        return fetchMaxBytes;
    }

    public void setFetchMaxBytes(int fetchMaxBytes) {
        this.fetchMaxBytes = fetchMaxBytes;
    }

    public boolean isAutoCommitEnable() {
        return autoCommitEnable;
    }

    public void setAutoCommitEnable(boolean autoCommitEnable) {
        this.autoCommitEnable = autoCommitEnable;
    }

    /*
    props.put("zookeeper.connect", "localhost:2181");
    props.put("group.id", "groupId");
    props.put("fetch.message.max.bytes", "1024x1024");
    props.put("zookeeper.session.timeout.ms", "400");
    props.put("zookeeper.sync.time.ms", "200");
    props.put("auto.commit.interval.ms", "1000");
    props.put("auto.commit.enable", false);
    */
}
