package org.mz.ditran.core.conf;

import lombok.Getter;
import lombok.Setter;
import org.mz.ditran.common.DitranConstants;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 18:47
 */
public class DitranZKConfig {
    @Getter
    private final String namespace = DitranConstants.NAMESPACE;
    @Setter
    @Getter
    private String serverLists;
    @Setter
    @Getter
    private int baseSleepTimeMilliseconds = 1000;
    @Setter
    @Getter
    private int maxSleepTimeMilliseconds = 3000;
    @Setter
    @Getter
    private int maxRetries = 3;
    @Setter
    @Getter
    private int sessionTimeoutMilliseconds;
    @Setter
    @Getter
    private int connectionTimeoutMilliseconds;
    @Setter
    @Getter
    private String digest;


}
