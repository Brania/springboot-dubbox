/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.util;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午8:49
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Slf4j
public abstract class AbstractExecutableTask implements Runnable {


    @Override
    public void run() {
        runInternal();
    }

    /**
     * 任务逻辑
     */
    protected abstract void runInternal();
}
