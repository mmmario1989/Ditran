package org.mz.ditran.core.conf;

import org.mz.ditran.core.DitranAspect;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 6:23 PM
 * @Description:
 */
public class DitranActiveContainer extends DitranContainer {

    private DitranAspect ditranAspect;

    public DitranActiveContainer(DitranZKConfig config) {
        super(config);
    }

    @Override
    protected void check() {

    }
}
