/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 . All rights reserved.
 *          刀光剑影不是我门派，
 *          天空海阔自有我风采。
 *          双手一推非黑也非白，
 *          不好也不坏，没有胜又何来败。
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.common;

import cn.zhangxd.platform.admin.web.domain.common.License;
import cn.zhangxd.platform.admin.web.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: 2017/8/26
 * Time: 下午1:18
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Aspect
@Slf4j
@Component
public class ProfileAspect implements InitializingBean {

    /**
     * 拦截配置存在问题
     */
    @Pointcut("bean(*Controller) && !bean(AuthenticationController) && !bean(SysUserController)")
    private void license() {

    }

    @Around("license()")
    public Object profileExecuteArchiveQuery(ProceedingJoinPoint jointPoint) throws Throwable {

        Signature signature = jointPoint.getSignature();
        String methodName = signature.toShortString();

        log.info(String.format("拦截接口 [ %s ]", methodName));

        Object r;

        License license = SecurityUtils.calculateLicense();
        if (license.getOnlineDays() > license.getLicenseDays()) {
            log.info(String.format("授权主体 [%s] 已过期，授权天数: [%d]天 系统运行天数: [%d]天", license.getSubject(), license.getLicenseDays(), license.getOnlineDays()));
            return null;
        }
        try {
            r = jointPoint.proceed();
        } finally {
            log.info("拦截结束;;;;");
        }
        return r;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
