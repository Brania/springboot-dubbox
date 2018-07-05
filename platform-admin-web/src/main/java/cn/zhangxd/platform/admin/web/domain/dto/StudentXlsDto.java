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
 * Time: 下午2:31
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */

@Data
public class StudentXlsDto implements Serializable {


    private static final long serialVersionUID = 2325943905353632626L;

    private String ksh;
    private String lqh;
    private String xh;
    private String xm;
    private String xb;
    private String mz;
    private String sfzh;
    private String zy;
    private String yx;
    private String bj;
    private String lqnf;
    private String txdz;
    private String yzbm;
    private String lxr;
    private String lxdh1;
    private String lxdh2;

    private String dah;
    private String sydq;
    private String daqx;
    private String jsdw;
    private String ydh;
    /**
     * 档案标记
     */
    private String remarks;

    /**
     * 毕业年份
     */
    private String graduationYear;


}
