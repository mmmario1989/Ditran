package org.mz.ditran.common;


import lombok.Getter;
import org.mz.ditran.common.enums.RpcType;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 5:31 PM
 * @Description:
 */
@Getter
public class DitranContext {
    private static final ThreadLocal<DitranContext> HOLDER = new ThreadLocal<>();

    private RpcType rpcType;
    private boolean ditranSwitch;

    private DitranContext(RpcType rpcType, boolean ditranSwitch) {
        this.rpcType = rpcType;
        this.ditranSwitch = ditranSwitch;
    }

    public static void set(RpcType rpcType, boolean ditranSwitch){
        HOLDER.set(new DitranContext(rpcType,ditranSwitch));
    }

    public static DitranContext get(){
        return HOLDER.get();
    }

    public static void clear(){
        HOLDER.remove();
    }
}
