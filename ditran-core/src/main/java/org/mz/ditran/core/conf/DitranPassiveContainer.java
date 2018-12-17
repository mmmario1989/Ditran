package org.mz.ditran.core.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

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

    public DitranPassiveContainer(String zkServerList) {
        super(zkServerList);
    }

    public DitranPassiveContainer() {
    }
}
