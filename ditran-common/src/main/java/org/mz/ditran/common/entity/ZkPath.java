package org.mz.ditran.common.entity;

import lombok.Data;

import java.io.File;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 12:26 PM
 * @Description:
 */
@Data
public class ZkPath {

    public static final String PREFIX = File.separator;

    private String fullMethodName;

    private String active;

    private String passive;

    public ZkPath(String path) {
        String[] elements = path.trim().split(PREFIX);
        fullMethodName = elements[1];
        if(elements.length>2){
            active = elements[2];
        }
        if(elements.length>3){
            passive = elements[3];
        }
    }

    public String getActivePath(){
        return buildPath(fullMethodName,active);

    }
    public String getPassivePath(){
        return buildPath(fullMethodName,active,passive);
    }

    public String getTransactionPath(){
        return buildPath(fullMethodName,active);
    }

    private String buildPath(String...element){
        StringBuilder path = new StringBuilder();
        for (String s : element) {
            path.append(PREFIX).append(s);
        }
        return path.toString();
    }
}
