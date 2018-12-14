package org.mz.ditran.core.conf;

import lombok.Getter;
import org.mz.ditran.core.DitranAspect;
import org.mz.ditran.core.transaction.DitransactionManager;
import org.mz.ditran.core.transaction.impl.ActiveDitransactionManager;
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

    @Getter
    private DitransactionManager ditransactionManager;

    public DitranActiveContainer(DitranZKConfig config, PlatformTransactionManager transactionManager,DitranAspect ditranAspect) {
        super(config, transactionManager);
        this.ditranAspect = ditranAspect;
        ditransactionManager = new ActiveDitransactionManager(transactionManager,ditranZKClient);
    }

    @Override
    protected void check() {
        super.check();
        if(ditranAspect==null){
            throw new IllegalArgumentException("ditranAspect can not be null");
        }
    }


}
