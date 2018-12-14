package org.mz.ditran.common.entity;

import lombok.Data;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 12:26 PM
 * @Description:
 */
@Data
public class ZkPath {

    private static String PREFIX ="/";

    private String namespace;

    private String transaction;

    private String node;

    public ZkPath(String namespace, String transaction) {
        this.namespace = namespace;
        this.transaction = transaction;
    }

    public ZkPath(String path) {
        String[] elements = path.trim().split(PREFIX);
        namespace = elements[1];
        if(elements.length>2){
            transaction = elements[2];
        }
        if(elements.length==4){
            node = elements[3];
        }
    }

    public String getFullPath(){
        return buildPath(namespace,transaction,node);
    }

    public String getTransactionPath(){
        return buildPath(namespace,transaction);
    }

    private String buildPath(String...element){
        StringBuilder path = new StringBuilder();
        for (String s : element) {
            path.append(PREFIX).append(s);
        }
        return path.toString();
    }
}
