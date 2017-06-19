/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.util;

import cn.zhangxd.platform.admin.web.security.model.AuthUser;
import cn.zhangxd.platform.common.web.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午8:37
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Slf4j
public class SecurityUtils {

    /**
     * 判断当前登录者身份是否为管理员
     *
     * @return
     */
    public static Boolean hasAdminRole() {
        AuthUser authUser = WebUtils.getCurrentUser();
        List<GrantedAuthority> grantedAuthorityList = authUser.getAuthorities().stream().filter(granted -> "ROLE_ADMIN".equals(String.valueOf(granted))).collect(Collectors.toList());
        return grantedAuthorityList.size() > 0 ? Boolean.TRUE : Boolean.FALSE;
    }
}
