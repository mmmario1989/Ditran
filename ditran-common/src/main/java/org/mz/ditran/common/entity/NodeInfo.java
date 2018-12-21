package org.mz.ditran.common.entity;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import lombok.Builder;
import lombok.Data;
import org.mz.ditran.common.DitranConstants;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 5:39 PM
 * @Description:
 */
@Data
@Builder
public class NodeInfo {

    private String host;

    private String className;

    private String methodName;

    private String status;

    private String[] paramTypes;

    private String pTransactionPath;

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

    public String getTransactionPath(){
        return ZkPath.PREFIX+className+"."+methodName+DitranConstants.ACTIVE_NODE;
    }
}
