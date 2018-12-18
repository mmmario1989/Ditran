package org.mz.ditran.core.transaction;

import org.mz.ditran.common.entity.NodeInfo;
import org.springframework.transaction.annotation.Propagation;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 11:20 AM
 * @Description:
 */
public interface DitransactionManager  {

    void begin(NodeInfo nodeInfo, Propagation propagation) throws Exception;

    void regist() throws Exception;

    void prepare() throws Exception;

    NodeInfo listen() throws Exception;

    void commit() throws Exception;

    void rollback() throws Exception;

}
