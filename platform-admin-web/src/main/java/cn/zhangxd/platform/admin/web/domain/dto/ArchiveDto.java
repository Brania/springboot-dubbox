/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.domain.dto;

import cn.zhangxd.platform.admin.web.domain.Depart;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午10:56
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Data
public class ArchiveDto implements Serializable {

    private static final long serialVersionUID = -4272491416650657021L;
    private Long id;
    private String examineeNo;
    private String admissionNo;
    private String studentNo;
    private String name;
    private Depart depart;
    private Integer entranceYear;
//  private String consistencyMark;
    private Long totalArchiveCount;
    private Long filledCount;


}
