package org.mz.ditran.common.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Charsets;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.mz.ditran.common.DitranConstants;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 5:39 PM
 * @Description:
 */
@Getter
@Setter
@Builder
public class NodeInfo {

    private String host;

    private String className;

    private String methodName;

    private String status;

    private String[] paramTypes;

    private String pTransactionPath;

    public NodeInfo(String host, String className, String methodName, String status, String[] paramTypes, String pTransactionPath) {
        this.host = host;
        this.className = className;
        this.methodName = methodName;
        this.status = status;
        this.paramTypes = paramTypes;
        this.pTransactionPath = pTransactionPath;
    }

    public NodeInfo() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public byte[] toBytes(){
        return this.toString().getBytes(Charsets.UTF_8);
    }

    public NodeInfo setFailed(){
        this.status = DitranConstants.ZK_NODE_FAIL_VALUE;
        return this;
    }
    public NodeInfo setSucceed(){
        this.status = DitranConstants.ZK_NODE_SUCCESS_VALUE;
        return this;
    }

    public static NodeInfo parse(String json){
        return JSON.parseObject(json,NodeInfo.class);
    }

    @JSONField(serialize = false)
    public String getTransactionPath(){
        return ZkPath.PREFIX+className+"."+methodName+ZkPath.PREFIX+DitranConstants.ACTIVE_NODE;
    }
}
