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
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午1:02
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * 转接事由 -> 类型
 */
@Data
public class TransmitEventTreeNode implements Serializable {


    private static final long serialVersionUID = -7302036143787891386L;
    /**
     * 父级编号
     */
    private String parentId;
    /**
     * 事由名称
     */
    private String name;

    private Boolean enabled;

    private String id;
    /**
     * 转接状态（针对类型EventType）
     */
    private String status;
    /**
     * 排序（针对类型EventType）
     */
    private Integer sort;


    private List<TransmitEventTreeNode> children = new ArrayList<>();



}
