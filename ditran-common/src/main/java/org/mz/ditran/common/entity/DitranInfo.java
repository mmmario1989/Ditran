package org.mz.ditran.common.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.transaction.TransactionStatus;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 12:58 PM
 * @Description:
 */
@Data
@Builder
public class DitranInfo {

    private ZkPath zkPath;

    private NodeInfo nodeInfo;

    private TransactionStatus transactionStatus;

}
