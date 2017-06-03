/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.domain;

import cn.zhangxd.platform.admin.web.util.IdEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午8:40
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description: 登录日志
 */
@Entity
@Table(name = "xz_login_log")
@Data
public class LoginLog extends IdEntity {

    private String userId;

    private String loginInfo;

    private Date loginTime;

    private String loginIp;


}
