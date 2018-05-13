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

package cn.zhangxd.platform.admin.web.domain.dto;

import cn.zhangxd.platform.admin.web.enums.SexEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 对REDIS中档案JSON字符串转换
 * {@link StudentBizDto}类中注解导致FASTJSON解析字符串失败，所以用此类解析.
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2017/9/17
 */
@Data
public class ArchiveOperatorDto implements Serializable {


    private static final long serialVersionUID = 517309860324965896L;
    /**
     * 考生号
     */
    private String examineeNo;

    /**
     * 录取年份
     */
    private Integer entranceYear;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private SexEnum sex;

    /**
     * 院系名称
     */
    private String depart;

    /**
     * 录取号
     */
    private String admissionNo;

    /**
     * 档案状态
     */
    private String status;

    /**
     * 操作日期
     */
    private LocalDateTime bizTime;


}
