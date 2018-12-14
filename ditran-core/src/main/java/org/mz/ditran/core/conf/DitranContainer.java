package org.mz.ditran.core.conf;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.mz.ditran.common.exception.DitranZKException;
import org.mz.ditran.core.zk.DitranZKClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 6:29 PM
 * @Description:
 */
@Slf4j
@Getter
@Component
public abstract class DitranContainer implements ApplicationContextAware {


    private static ApplicationContext context;

    protected static PlatformTransactionManager transactionManager;

    private CuratorFramework client;

    protected DitranZKClient ditranZKClient;

    private DitranZKConfig config;

    @Autowired
    public DitranContainer(DitranZKConfig config, PlatformTransactionManager transactionManager) {
        this.config = config;
        DitranContainer.transactionManager = transactionManager;
    }

    /**
     * 获取zk客户端
     * @return
     */
    public DitranZKClient getZkClient(){
        return this.ditranZKClient;
    }

    @PostConstruct
    protected void init(){
        check();
        initZk();
    }

    private void initZk() {
        log.info("Ditran: zookeeper client init, server lists is: {}.", config.getServerLists());

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(this.config.getServerLists())
                .retryPolicy(new ExponentialBackoffRetry(this.config.getBaseSleepTimeMilliseconds(), this.config.getMaxRetries(), this.config.getMaxSleepTimeMilliseconds()))
                .namespace(this.config.getNamespace());
        if (0 != this.config.getSessionTimeoutMilliseconds()) {
            builder.sessionTimeoutMs(this.config.getSessionTimeoutMilliseconds());
        }

        if (0 != this.config.getConnectionTimeoutMilliseconds()) {
            builder.connectionTimeoutMs(this.config.getConnectionTimeoutMilliseconds());
        }

        if (!Strings.isNullOrEmpty(this.config.getDigest())) {
            builder.authorization("digest", this.config.getDigest().getBytes(Charsets.UTF_8)).aclProvider(new ACLProvider() {
                public List<ACL> getDefaultAcl() {
                    return ZooDefs.Ids.CREATOR_ALL_ACL;
                }

                public List<ACL> getAclForPath(String path) {
                    return ZooDefs.Ids.CREATOR_ALL_ACL;
                }
            });
        }

        this.client = builder.build();
        this.client.start();

        this.ditranZKClient = new DitranZKClient(this.client, config.getNamespace());

        try {
            if (!this.client.blockUntilConnected(this.config.getMaxSleepTimeMilliseconds() * this.config.getMaxRetries(), TimeUnit.MILLISECONDS)) {
                this.client.close();
                throw new KeeperException.OperationTimeoutException();
            }
        } catch (Exception e) {
            log.error("Register zk exception.", e);
            throw new DitranZKException(e);
        }
    }

    @PreDestroy
    public void close() {
        CloseableUtils.closeQuietly(client);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T extends DitranContainer> T getConfig(Class<T> config){
        return context.getBean(config);
    }

    /**
     * 基本参数验证
     *
     * 报错跑出RuntimeException
     *
     */
    protected void check(){
        if (config == null) {
            throw new IllegalArgumentException("Zk config is null!");
        }
        if (StringUtils.isBlank(config.getServerLists()) || StringUtils.isBlank(config.getNamespace())) {
            throw new IllegalArgumentException("Zk config missing server list or namespace!");
        }
        Assert.notNull(transactionManager,"transactionManager can not be null");
    }


    public static PlatformTransactionManager getTransactionManager(){
        return transactionManager;
    }
}
