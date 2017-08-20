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
import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午9:36
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Data
public class TransmitRecordDto implements Serializable {

    private static final long serialVersionUID = -9000661225447336101L;

    private Long id;

    /**
     * 转接类型名称::转入::转出
     */
    private String eventTypeName;
    /**
     * 转接事由名称
     */
    private String eventName;
    /**
     * 转接时间
     */
    private Date operTime;

    /**
     * 经办人
     */
    private String operUser;

    private String transmitForm;

    private String transmitTo;

    private String remarks;

    private String contexts;


}
