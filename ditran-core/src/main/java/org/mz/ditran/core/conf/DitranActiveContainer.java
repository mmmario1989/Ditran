package org.mz.ditran.core.conf;

import lombok.Setter;
import org.mz.ditran.core.DitranAspect;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 6:23 PM
 * @Description:
 */
public class DitranActiveContainer extends DitranContainer {

    @Setter
    private DitranAspect ditranAspect;

    public DitranActiveContainer(String zkServerList, DitranAspect ditranAspect) {
        super(zkServerList);
        this.ditranAspect = ditranAspect;
    }

    public DitranActiveContainer(){}

    @Override
    protected void check() {
        super.check();
        if(ditranAspect==null){
            throw new IllegalArgumentException("ditranAspect can not be null");
        }
    }


}
