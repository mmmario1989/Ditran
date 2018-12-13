package org.mz.ditran.core.zk;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.mz.ditran.common.exception.DitranZKException;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 19:02
 */
@Slf4j
public class DitranZKClient {

    private static final String ROOT = "/";

    private CuratorFramework client;

    private String prefix;

    public DitranZKClient(CuratorFramework client, String namespace) {
        this.client = client;
        this.prefix = ROOT + namespace;
    }

    /**
     * 向某个节点写数据
     * @param key
     */
    public void persist(final String key, final String value) {
        try {
            if (isExisted(key)) {
                update(key, value);
                return;
            }

            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key, value.getBytes(Charsets.UTF_8));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DitranZKException(String.format("Persist zk path failed.Path:{}, Value:{}", key, value), e);
        }
    }

    /**
     * 修改节点值
     * @param key
     * @param value
     */
    public void update(final String key, final String value) {
        try {
            client.setData().forPath(key, value.getBytes(Charsets.UTF_8));
        } catch (Exception e) {
            throw new DitranZKException(String.format("Update zk path failed.Path:{}, Value:{}.", key, value), e);
        }
    }

    /**
     * 检查节点是否存在
     * @param key
     * @return
     */
    public boolean isExisted(final String key) {
        try {
            return client.checkExists().forPath(key) != null;
        } catch (Exception e) {
            log.error("Check zk path failed.Path:{}.", key, e);
            return false;
        }
    }

    /**
     * 获取节点的值
     * @param key
     * @return
     */
    public String get(final String key) {
        try {
            byte[] bytes = client.getData().forPath(key);
            return new String(bytes);
        } catch (Exception e) {
            throw new DitranZKException(String.format("Get data from zk failed.Path: %s.", key), e);
        }
    }

    /**
     * 获取前缀
     * @return
     */
    public String getPrefix() {
        return prefix;
    }
}
