/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.common.web.interceptor;

import cn.zhangxd.platform.common.web.annotations.Action;
import cn.zhangxd.platform.common.web.annotations.License;
import cn.zhangxd.platform.common.web.annotations.LicenseSubject;
import cn.zhangxd.platform.common.web.util.LicenseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午3:18
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */

@Slf4j
public class LicenseInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 处理 Controller 方法
         * <p>
         * 判断 handler 是否为 HandlerMethod 实例
         * </p>
         */
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            License license = method.getAnnotation(License.class);
//            if (login != null) {
//                if (login.action() == Action.Skip) {
//                    /**
//                     * 忽略拦截
//                     */
//                    return Boolean.TRUE;
//                }
//            }

            if (license != null && license.action() == Action.Check) {
                /**
                 * 执行鉴权
                 */
                LicenseSubject subject = LicenseUtils.check();
                if (subject.getOnlineDays() > subject.getLicenseDays()) {
                    log.info(String.format("授权主体 [%s] 已过期，授权天数: [%d]天 系统运行天数: [%d]天", subject.getSubject(), subject.getLicenseDays(), subject.getOnlineDays()));
                    return Boolean.FALSE;
                }
            }
        }

        /**
         * 通过拦截
         */
        return Boolean.TRUE;
    }
}
