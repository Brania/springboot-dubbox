/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.domain.dto;

import cn.zhangxd.platform.admin.web.enums.NationalityEnum;
import cn.zhangxd.platform.admin.web.enums.SexEnum;
import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午8:24
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Data
public class StudentDetailDto implements Serializable {

    private static final long serialVersionUID = 1437301548785209490L;

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
     * 专业
     */
    private String major;
    /**
     * 院系
     */
    private String depart;
    /**
     * 班级
     */
    private String adClass;
    /**
     * 录取年份
     */
    private Integer entranceYear;

    /**
     * 毕业年份
     */
    private Integer graduationYear;
    /**
     * 家庭地址
     */
    private String familyAddress;
    /**
     * 邮编
     */
    private String postCode;
    /**
     * 联系人
     */
    private String linkPerson;
    /**
     * 联系电话
     */
    private String primaryPhone;
    /**
     * 备用联系电话
     */
    private String backupPhone;

    private TransmitEnum status;

    private List<TransmitRecordDto> records = Lists.newArrayList();

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
