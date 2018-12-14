package org.mz.ditran.core.transaction;

import lombok.extern.slf4j.Slf4j;
import org.mz.ditran.common.Handler;
import org.mz.ditran.common.entity.DitranInfo;
import org.mz.ditran.common.entity.ZkPath;
import org.mz.ditran.common.exception.DitransactionException;
import org.springframework.transaction.annotation.Propagation;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 12:42 PM
 * @Description:
 *
 */
@Slf4j
public class DitransactionWrapper<PARAM,RES> {

    private Handler<PARAM,RES> handler;

    private DitransactionManager transactionManager;



    public static class DitransactionWrapperBuilder<PARAM,RES>{
        private Handler<PARAM,RES> handler;

        public DitransactionWrapper<PARAM,RES> with(DitransactionManager ditransactionManager){
            DitransactionWrapper<PARAM,RES> wrapper = new DitransactionWrapper<>();
            wrapper.handler = this.handler;
            wrapper.transactionManager = ditransactionManager;
            return wrapper;
        }
    }


    private DitransactionWrapper() {
    }

    public static<PARAM,RES> DitransactionWrapperBuilder<PARAM,RES> wrap(Handler<PARAM,RES> handler){
        DitransactionWrapperBuilder<PARAM,RES> builder = new DitransactionWrapperBuilder<>();
        builder.handler = handler;
        return builder;
    }

    public RES start(String methodName, Propagation propagation,PARAM param) throws Throwable {
        DitranInfo ditranInfo = transactionManager.begin(methodName,propagation);
        ZkPath zkPath = transactionManager.regist(ditranInfo.getZkPath());
        ditranInfo.setZkPath(zkPath);
        try{
            RES res = handler.handle(param);
            transactionManager.prepare(ditranInfo.getZkPath());
            boolean succeed = transactionManager.listen(ditranInfo.getZkPath());
            if(succeed){
                transactionManager.commit(ditranInfo);
            }else {
                throw new DitransactionException("other node failed");
            }
            return res;
        }catch (Throwable e){
            log.info(e.getMessage(),e);
            transactionManager.rollback(ditranInfo);
            throw e;
        }

    }
}
