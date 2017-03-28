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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午4:07
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * 专业
 */
@Entity
@Table(name = "xz_major")
@Data
public class Major extends IdEntity {

    @Column(name = "code", unique = true)
    private String code;
    @Column(name = "name", unique = true)
    private String name;

    private Boolean enabled;

}
