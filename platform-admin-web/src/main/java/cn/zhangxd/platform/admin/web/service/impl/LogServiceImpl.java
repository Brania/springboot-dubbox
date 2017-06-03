/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service.impl;

import cn.zhangxd.platform.admin.web.domain.LoginLog;
import cn.zhangxd.platform.admin.web.repository.LoginLogRepository;
import cn.zhangxd.platform.admin.web.service.LogService;

import cn.zhangxd.platform.common.api.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午9:21
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Service
@Slf4j
public class LogServiceImpl implements LogService {


    @Autowired
    private LoginLogRepository loginLogRepository;

    @Override
    public void saveLoginLog(LoginLog log) {
        loginLogRepository.save(log);
    }


    @Override
    public Page<LoginLog> getLoginLogPages(Map<String, Object> searchParams, Paging paging) {

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(paging.getPageNum(), paging.getPageSize(), sort);
        return loginLogRepository.findAll(pageable);
    }
}
