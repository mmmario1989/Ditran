package org.mz.ditran.common;


import lombok.Getter;
import org.mz.ditran.common.entity.ZkPath;
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
    private ZkPath zkPath;


    public static void setRpcType(RpcType rpcType){
        get().rpcType = rpcType;
    }

    public static void setZkPath(ZkPath zkPath){
        get().zkPath = zkPath;
    }

    public static DitranContext get(){
        DitranContext context =  HOLDER.get();
        if(context==null){
            context = new DitranContext();
            HOLDER.set(context);
        }
        return context;
    }

    public static void clear(){
        HOLDER.remove();
    }
}
