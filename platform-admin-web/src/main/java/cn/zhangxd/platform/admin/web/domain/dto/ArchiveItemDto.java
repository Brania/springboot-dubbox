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
 * Time: 下午11:04
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Data
public class ArchiveItemDto implements Serializable {


    private static final long serialVersionUID = -5193670353917637312L;

    private Long id;

    private String name;
    private String remarks;
    /**
     * 使用状态
     */
    private Boolean enabled;
    /**
     * 是否必备
     */
    private Boolean forced;

    private Integer sort;


    private Long classifyId;

    private String classifyName;

    private Integer rowSpan = 0;

    /**
     * 档案是否存在标识::相对学生
     */
    private Boolean fileExist = Boolean.FALSE;
    /**
     * 档案提交时间
     */
    private Date createTime;


}
