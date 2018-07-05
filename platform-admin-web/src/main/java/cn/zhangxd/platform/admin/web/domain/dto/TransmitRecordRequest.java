/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午10:48
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Data
public class TransmitRecordRequest implements Serializable{
    private static final long serialVersionUID = 3147168976513148596L;

    /**
     * 转接记录ID（防止产生重复转接记录）
     */
    private Long recId;
    /**
     * 保管人
     */
    private String custodian;
    /**
     * 转办类型
     */
    private String eventTypeId;
    /**
     * 存放地点
     */
    private String remarks;
    /**
     * 考生号、姓名、身份证号
     */
    private String searchParam;
    /**
     * 是否单个操作
     */
    private Boolean single;
    /**
     * 接收院系、机构
     */
    private String toDepart;
    /**
     * 根据院系查询学生，用于批量操作
     */
    private String depart;

    /**
     * 批量办理档案标记
     */
    private String requestParam;


}
