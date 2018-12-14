package org.mz.ditran.core.conf;

import lombok.Data;

import java.beans.ConstructorProperties;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 18:47
 */
@Data
public class DitranZKConfig {
    private final String serverLists;
    private final String namespace;
    private int baseSleepTimeMilliseconds = 1000;
    private int maxSleepTimeMilliseconds = 3000;
    private int maxRetries = 3;
    private int sessionTimeoutMilliseconds;
    private int connectionTimeoutMilliseconds;
    private String digest;

    @ConstructorProperties({"serverLists", "namespace"})
    public DitranZKConfig(String serverLists, String namespace) {
        this.serverLists = serverLists;
        this.namespace = namespace;
    }
}
