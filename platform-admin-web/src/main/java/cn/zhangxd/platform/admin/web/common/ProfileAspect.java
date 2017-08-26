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

import cn.zhangxd.platform.admin.web.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

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

    @Around("within(cn.zhangxd.platform.admin.web.controller.ArchiveController)")
    public Object profileExecuteArchiveQuery(ProceedingJoinPoint jointPoint) throws Throwable {

        Signature signature = jointPoint.getSignature();
        String methodName = signature.toShortString();

        if (StringUtils.equals(Constants.ARCHIVE_QUERY_METHOD, methodName)) {
            log.info(String.format(";;;;拦截接口 [ %s ]", methodName));
        }

        Object r = null;
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
