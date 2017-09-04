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

import cn.zhangxd.platform.admin.web.enums.NationalityEnum;
import cn.zhangxd.platform.admin.web.enums.SexEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 表单提交保存学生
 *
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2017/9/4
 */
@Data
public class StudentRequestForm implements Serializable {

    private static final long serialVersionUID = -4563203193749159846L;

    private Long id;

    /**
     * 考生号
     */
    private String examineeNo;
    /**
     * 录取号
     */
    private String admissionNo;
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
     * 民族
     */
    private NationalityEnum nationality;
    /**
     * 身份证号码
     */
    private String idCard;
    /**
     * 专业名称
     */
    private String major;
    /**
     * 院系名称
     */
    private String depart;
    /**
     * 班级名称
     */
    private String adClass;
    /**
     * 录取年份
     */
    private Integer entranceYear;
    /**
     * 家庭地址->档案邮寄通信地址
     */
    private String familyAddress;
    /**
     * 邮编
     */
    private String postCode;
    /**
     * 联系人->收件人
     */
    private String linkPerson;
    /**
     * 联系电话->收件人联系电话1
     */
    private String primaryPhone;
    /**
     * 备用联系电话->收件人联系电话2
     */
    private String backupPhone;
    /**
     * 数据来源：IMPORT/ADD
     */
    private String sources;
    /**
     * 档案号-add
     */
    private String archiveNo;
    /**
     * 生源地区-add
     */
    private String sourceRegion;
    /**
     * 档案去向-add
     */
    private String archiveGonePlace;
    /**
     * 档案接收单位-add
     */
    private String receiveUnit;
    /**
     * 运单号-add
     */
    private String trackNo;
}
