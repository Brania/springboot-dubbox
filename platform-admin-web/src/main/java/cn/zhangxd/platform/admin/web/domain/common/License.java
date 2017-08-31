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

package cn.zhangxd.platform.admin.web.domain.common;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;

/**
 * 软件授权
 *
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2017/8/30
 */
@Data
public class License implements Serializable {

    private static final long serialVersionUID = -2890563474948601218L;
    /**
     * 授权主体
     */
    private String subject = "南京晓庄学院";
    /**
     * 剩余天数
     */
    private Long licenseDays = 60l;
    /**
     * 剩余天数（根据当前时间动态计算）
     */
    private Long limitDays = 0l;
    /**
     * 系统运行天数
     */
    private Long onlineDays = 0l;
    /**
     * 剩余不足天数开始告警
     */
    private Integer warnDays = 10;

    private LocalDate buyTime = LocalDate.of(2017, Month.AUGUST, 30);
    /**
     * 是否免费
     */
    private Boolean isFree;

    private Boolean warning = Boolean.FALSE;

}
