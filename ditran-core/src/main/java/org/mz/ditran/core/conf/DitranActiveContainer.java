package org.mz.ditran.core.conf;

import org.mz.ditran.core.DitranAspect;
import org.springframework.util.Assert;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 6:23 PM
 * @Description:
 */
public class DitranActiveContainer extends DitranContainer {

    private DitranAspect ditranAspect;

    public DitranActiveContainer(DitranZKConfig config, PlatformTransactionManager transactionManager) {
        super(config, transactionManager);
    }

    @Override
    protected void check() {
        super.check();
        Assert.notNull(ditranAspect,"ditranAspect can not be null");
    }
}
