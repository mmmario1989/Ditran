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

    public static final String PREFIX ="/";

    private String transaction;

    private String node;

    public ZkPath(String path) {
        String[] elements = path.trim().split(PREFIX);
        transaction = elements[1];
        if(elements.length>2){
            node = elements[2];
        }
    }

    public String getFullPath(){
        return buildPath(transaction,node);
    }

    public String getTransactionPath(){
        return buildPath(transaction);
    }

    private String buildPath(String...element){
        StringBuilder path = new StringBuilder();
        for (String s : element) {
            path.append(PREFIX).append(s);
        }
        return path.toString();
    }
}
