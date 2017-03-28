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
 * Time: 上午9:23
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Slf4j
@Data
public class LogImpExcel implements Serializable {


    private static final long serialVersionUID = 7575247949698371454L;

    private Boolean hasError = Boolean.FALSE;
    /**
     * 导入记录条数
     */
    private Integer totalSize;

    /**
     * 导入成功记录条数
     */
    private Integer successSize;
}
