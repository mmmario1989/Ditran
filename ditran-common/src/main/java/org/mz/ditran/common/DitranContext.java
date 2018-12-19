package org.mz.ditran.common;


import lombok.Getter;
import org.mz.ditran.common.entity.ZkPath;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 5:31 PM
 * @Description:
 */
@Getter
public class DitranContext {
    private static final ThreadLocal<DitranContext> HOLDER = new ThreadLocal<>();

    private ZkPath zkPath;

    private long timeout;

    private String parentTransaction;

    public static void setZkPath(ZkPath zkPath){
        get().zkPath = zkPath;
    }

    public static void setParentTransaction(String parentTransaction){
        get().parentTransaction = parentTransaction;
    }

    public static void setTimeout(long timeout){
        get().timeout = timeout;
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
