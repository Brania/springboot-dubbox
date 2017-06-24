/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service.impl;

import cn.zhangxd.platform.admin.web.domain.OperatorLog;
import cn.zhangxd.platform.admin.web.repository.OperatorLogRepository;
import cn.zhangxd.platform.admin.web.service.OperatorLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 上午9:51
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Service
@Slf4j
public class OperatorLogServiceImpl implements OperatorLogService {

    @Autowired
    private OperatorLogRepository operatorLogRepository;

    @Override
    public void saveOperatorLog(OperatorLog operatorLog) {

        operatorLogRepository.save(operatorLog);

    }
}
