package org.mz.ditran.core.transaction;

import com.google.common.base.Function;
import lombok.extern.slf4j.Slf4j;
import org.mz.ditran.common.entity.DitranInfo;
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

    private Function<PARAM,RES> function;

    private DitransactionManager transactionManager;



    public static class DitransactionWrapperBuilder<PARAM,RES>{
        private Function<PARAM,RES> function;

        public DitransactionWrapper<PARAM,RES> with(DitransactionManager ditransactionManager){
            DitransactionWrapper<PARAM,RES> wrapper = new DitransactionWrapper<>();
            wrapper.function = this.function;
            wrapper.transactionManager = ditransactionManager;
            return wrapper;
        }
    }


    private DitransactionWrapper() {
    }

    public static<PARAM,RES> DitransactionWrapperBuilder<PARAM,RES> wrap(Function<PARAM,RES> function){
        DitransactionWrapperBuilder<PARAM,RES> builder = new DitransactionWrapperBuilder<>();
        builder.function = function;
        return builder;
    }

    public RES start(String methodName, Propagation propagation,PARAM param) throws Exception {
        DitranInfo ditranInfo = transactionManager.begin(methodName,propagation);
        transactionManager.regist(ditranInfo.getZkPath());
        RES res;
        try{
            res = function.apply(param);
            transactionManager.prepare(ditranInfo.getZkPath());
            boolean succeed = transactionManager.listen(ditranInfo.getZkPath());
            if(succeed){
                transactionManager.commit(ditranInfo);
            }else {
                throw new DitransactionException("other node failed");
            }
            return res;
        }catch (Exception e){
            log.info(e.getMessage(),e);
            transactionManager.rollback(ditranInfo);
            throw e;
        }

    }
}
