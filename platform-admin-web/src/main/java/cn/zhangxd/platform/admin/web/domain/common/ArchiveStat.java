/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.domain.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午7:44
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description: 档案统计
 */
@Slf4j
@Data
public class ArchiveStat implements Serializable {


    private static final long serialVersionUID = -1666329497790124952L;

    /**
     * 院系机构名称
     */
    private String dname;
    /**
     * 总档案数
     */
    private Integer totalAmount;

    /**
     * 已接收档案数
     */
    private Integer acceptedAmount;
    /**
     * 待接收档案数
     */
    private Integer waitingAmount;
}
