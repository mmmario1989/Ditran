package org.mz.ditran.core.conf;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.mz.ditran.common.exception.DitranInitException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 *
 * Passive端配置bean，需要注册到spring
 *
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 6:27 PM
 * @Description:
 */
@Data
public class DitranPassiveContainer extends DitranContainer {
    private DataSourceTransactionManager manager;

    public DitranPassiveContainer(DitranZKConfig config, DataSourceTransactionManager manager) {
        super(config);
        this.manager = manager;
    }

    @Override
    protected void check() {
        if (config == null) {
            throw new DitranInitException("Zk config is null!");
        }

        if (StringUtils.isBlank(config.getServerLists()) || StringUtils.isBlank(config.getNamespace())) {
            throw new DitranInitException("Zk config missing server list or namespace!");
        }

        if (manager == null) {
            throw new DitranInitException("Passive side must config the DataSourceTransactionManager!");
        }
    }
}
