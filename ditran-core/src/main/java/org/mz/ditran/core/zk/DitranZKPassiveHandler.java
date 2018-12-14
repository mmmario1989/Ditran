package org.mz.ditran.core.zk;

import lombok.extern.slf4j.Slf4j;
import org.mz.ditran.common.Constants;
import org.mz.ditran.common.exception.DitranZKException;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 21:20
 */
@Slf4j
public class DitranZKPassiveHandler extends DitranZKHandler {

    private String passiveKey;

    private String activeKey;

    public DitranZKPassiveHandler(DitranZKClient client, String transactionId, String node) {
        super(client);
        this.passiveKey = new StringBuilder().append(this.client.getPrefix())
                .append("/").append(transactionId)
                .append("/").append(Constants.PASSIVE_NODE)
                .append("/").append(node)
                .toString();
        this.activeKey = new StringBuilder().append(this.client.getPrefix())
                .append("/").append(transactionId)
                .append("/").append(Constants.ACTIVE_NODE)
                .toString();
    }

    @Override
    public void register() throws DitranZKException {
        client.persist(this.passiveKey, Constants.ZK_NODE_FAIL_VALUE);
    }

    /**
     * passive端的check需要阻塞，启动listener来监听active的状态
     *
     * 需设置超时时间
     *
     * @return
     */
    @Override
    public boolean check() {
        Future<Boolean> future = Executors.newFixedThreadPool(1).submit(new ActiveStatusCheckTask(client, activeKey));
        try {
            return future.get(client.getPassiveTimeout(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Check active exception.", e.getMessage());
        }
        return false;
    }

    @Override
    public void write(String value) throws DitranZKException {
        client.update(this.passiveKey, value);
    }

    /**
     * 检查Active端的状态
     */
    private static class ActiveStatusCheckTask implements Callable<Boolean> {
        private DitranZKClient client;

        private String activeKey;

        public ActiveStatusCheckTask(DitranZKClient client, String activeKey) {
            this.client = client;
            this.activeKey = activeKey;
        }

        @Override
        public Boolean call() throws Exception {
            String result;
            do {
                result = client.get(this.activeKey);
            } while(!Constants.ZK_NODE_SUCCESS_VALUE.equals(result));

            return true;
        }
    }

}
