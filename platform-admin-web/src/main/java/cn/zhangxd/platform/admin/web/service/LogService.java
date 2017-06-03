/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service;

import cn.zhangxd.platform.admin.web.domain.LoginLog;
import cn.zhangxd.platform.common.api.Paging;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午9:20
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public interface LogService {
    /**
     * 保存登录日志
     *
     * @param log
     */
    void saveLoginLog(LoginLog log);


    Page<LoginLog> getLoginLogPages(Map<String, Object> searchParams, Paging paging);
}
