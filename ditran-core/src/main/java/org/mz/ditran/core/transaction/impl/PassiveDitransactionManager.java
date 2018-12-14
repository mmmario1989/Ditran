package org.mz.ditran.core.transaction.impl;

import org.mz.ditran.common.DitranConstants;
import org.mz.ditran.common.entity.DitranInfo;
import org.mz.ditran.common.entity.NodeInfo;
import org.mz.ditran.common.entity.ZkPath;
import org.mz.ditran.core.transaction.DitransactionManagerAdapter;
import org.mz.ditran.core.zk.DitranZKClient;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 12:46 PM
 * @Description:
 */
public class PassiveDitransactionManager extends DitransactionManagerAdapter {

    private ZkPath activePath;


    public PassiveDitransactionManager(PlatformTransactionManager transactionManager, DitranZKClient zkClient, ZkPath activePath) {
        super(transactionManager, zkClient);
        this.activePath = activePath;
    }

    @Override
    public void begin(NodeInfo nodeInfo, Propagation propagation) throws Exception {
        ZkPath zkPath = new ZkPath(activePath.getNamespace(),activePath.getTransaction());
        zkPath.setNode(DitranConstants.PASSIVE_NODE);
        TransactionStatus transactionStatus = beginLocal(propagation);
        ditranInfo = DitranInfo.builder()
                .transactionStatus(transactionStatus)
                .zkPath(zkPath)
                .nodeInfo(nodeInfo)
                .build();
    }

    @Override
    public boolean listen() throws Exception{
        return false;
    }


    @Override
    public void commit() throws Exception{

    }
}
