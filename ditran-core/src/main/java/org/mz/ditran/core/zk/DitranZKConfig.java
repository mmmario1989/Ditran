package org.mz.ditran.core.zk;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 17:37
 */
public class DitranZKConfig {

    public DitranZKConfig(String address, String namespace) {
        this.address = address;
        this.namespace = namespace;
    }

    private String address;
    private String namespace;

}
