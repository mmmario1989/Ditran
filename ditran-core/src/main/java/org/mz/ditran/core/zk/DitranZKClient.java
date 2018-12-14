package org.mz.ditran.core.zk;

import com.google.common.base.Charsets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.mz.ditran.common.exception.DitranZKException;
import org.mz.ditran.core.conf.DitranZKConfig;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 19:02
 */
@Slf4j
public class DitranZKClient {

    private static final String ROOT = "/";

    @Getter
    private CuratorFramework client;
    @Getter
    private String prefix;

    private DitranZKConfig config;

    public DitranZKClient(CuratorFramework client, DitranZKConfig config) {
        this.client = client;
        this.prefix = ROOT + config.getNamespace();
        this.config = config;
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
            if (isExisted(key)) {
                byte[] bytes = client.getData().forPath(key);
                return new String(bytes);
            }

            return StringUtils.EMPTY;
        } catch (Exception e) {
            throw new DitranZKException(String.format("Get data from zk failed.Path: %s.", key), e);
        }
    }


    /**
     * 获取passive端超时时间
     * @return
     */
    public long getPassiveTimeout() {
        return config.getPassiveTimeoutMilliseconds();
    }
}
