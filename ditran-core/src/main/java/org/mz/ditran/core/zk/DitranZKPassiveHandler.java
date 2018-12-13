package org.mz.ditran.core.zk;

import org.mz.ditran.common.Constants;
import org.mz.ditran.common.exception.DitranZKException;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 21:20
 */
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
        String value = client.get(this.activeKey);
        if (Constants.ZK_NODE_SUCCESS_VALUE.equals(value)) {
            return true;
        }
        return false;
    }

    @Override
    public void write(String value) throws DitranZKException {
        client.update(this.passiveKey, value);
    }

}
