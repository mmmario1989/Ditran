package org.mz.ditran.core.transaction;

import org.mz.ditran.common.entity.DitranInfo;
import org.mz.ditran.common.entity.ZkPath;
import org.springframework.transaction.annotation.Propagation;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 11:20 AM
 * @Description:
 */
public interface DitransactionManager  {


    DitranInfo begin(String methodName, Propagation propagation) throws Exception;

    ZkPath regist(ZkPath zkPath);

    void prepare(ZkPath zkPath);

    boolean listen(ZkPath zkPath);

    void commit(DitranInfo ditranInfo);

    void rollback(DitranInfo ditranInfo);

}
