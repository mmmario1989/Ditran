package org.mz.ditran.common;

import org.mz.ditran.common.enums.RpcType;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 5:31 PM
 * @Description:
 */
public class DitranThreadContext {
    private static final ThreadLocal<DitranThreadContext> HOLDER = new ThreadLocal<>();

    private RpcType rpcType;
    private boolean ditranSwitch;

    private DitranThreadContext(RpcType rpcType, boolean ditranSwitch) {
        this.rpcType = rpcType;
        this.ditranSwitch = ditranSwitch;
    }

    public static void set(RpcType rpcType, boolean ditranSwitch){
        HOLDER.set(new DitranThreadContext(rpcType,ditranSwitch));
    }

    public static DitranThreadContext get(){
        return HOLDER.get();
    }

    public static void clear(){
        HOLDER.remove();
    }
}
