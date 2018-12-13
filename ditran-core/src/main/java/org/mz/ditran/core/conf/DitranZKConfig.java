package org.mz.ditran.core.conf;

import java.beans.ConstructorProperties;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 18:47
 */
public class DitranZKConfig {
    private final String serverLists;
    private final String namespace;
    private int baseSleepTimeMilliseconds = 1000;
    private int maxSleepTimeMilliseconds = 3000;
    private int maxRetries = 3;
    private int sessionTimeoutMilliseconds;
    private int connectionTimeoutMilliseconds;
    private String digest;

    public String getServerLists() {
        return this.serverLists;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public int getBaseSleepTimeMilliseconds() {
        return this.baseSleepTimeMilliseconds;
    }

    public int getMaxSleepTimeMilliseconds() {
        return this.maxSleepTimeMilliseconds;
    }

    public int getMaxRetries() {
        return this.maxRetries;
    }

    public int getSessionTimeoutMilliseconds() {
        return this.sessionTimeoutMilliseconds;
    }

    public int getConnectionTimeoutMilliseconds() {
        return this.connectionTimeoutMilliseconds;
    }

    public String getDigest() {
        return this.digest;
    }

    public void setBaseSleepTimeMilliseconds(int baseSleepTimeMilliseconds) {
        this.baseSleepTimeMilliseconds = baseSleepTimeMilliseconds;
    }

    public void setMaxSleepTimeMilliseconds(int maxSleepTimeMilliseconds) {
        this.maxSleepTimeMilliseconds = maxSleepTimeMilliseconds;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setSessionTimeoutMilliseconds(int sessionTimeoutMilliseconds) {
        this.sessionTimeoutMilliseconds = sessionTimeoutMilliseconds;
    }

    public void setConnectionTimeoutMilliseconds(int connectionTimeoutMilliseconds) {
        this.connectionTimeoutMilliseconds = connectionTimeoutMilliseconds;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    @ConstructorProperties({"serverLists", "namespace"})
    public DitranZKConfig(String serverLists, String namespace) {
        this.serverLists = serverLists;
        this.namespace = namespace;
    }
}
