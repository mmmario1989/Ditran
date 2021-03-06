package org.mz.ditran.core.transaction;

import lombok.extern.slf4j.Slf4j;
import org.mz.ditran.common.DitranConstants;
import org.mz.ditran.common.Handler;
import org.mz.ditran.common.entity.NodeInfo;
import org.mz.ditran.common.exception.DitransactionException;
import org.springframework.transaction.annotation.Propagation;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 12:42 PM
 * @Description:
 */
@Slf4j
public class DitransactionWrapper<PARAM, RES> {

    private Handler<PARAM, RES> handler;

    private DitransactionManager transactionManager;

    private DitransactionWrapper() {
    }

    public static class DitransactionWrapperBuilder<PARAM, RES> {
        private Handler<PARAM, RES> handler;

        public DitransactionWrapper<PARAM, RES> with(DitransactionManager ditransactionManager) {
            DitransactionWrapper<PARAM, RES> wrapper = new DitransactionWrapper<>();
            wrapper.handler = this.handler;
            wrapper.transactionManager = ditransactionManager;
            return wrapper;
        }
    }


    public static <PARAM, RES> DitransactionWrapperBuilder<PARAM, RES> wrap(Handler<PARAM, RES> handler) {
        DitransactionWrapperBuilder<PARAM, RES> builder = new DitransactionWrapperBuilder<>();
        builder.handler = handler;
        return builder;
    }

    public RES start(NodeInfo nodeInfo, Propagation propagation, PARAM param) throws Throwable {
        transactionManager.begin(nodeInfo,propagation);
        transactionManager.register();
        try {
            RES res = handler.handle(param);
            transactionManager.prepare();
            NodeInfo info = transactionManager.listen();
            if (DitranConstants.ZK_NODE_SUCCESS_VALUE.equals(info.getStatus())) {
                transactionManager.commit();
            } else {
                throw new DitransactionException("Other node failed:"+info.toString());
            }
            return res;
        } catch (Throwable e) {
            log.info(e.getMessage(), e);
            transactionManager.rollback();
            throw e;
        }

    }
}
