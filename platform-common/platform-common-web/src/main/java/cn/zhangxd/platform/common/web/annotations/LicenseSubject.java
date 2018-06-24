package cn.zhangxd.platform.common.web.annotations;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;

/**
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2017/10/15
 */
@Data
public class LicenseSubject implements Serializable {

    private static final long serialVersionUID = 2005655841527499065L;

    /**
     * 授权主体
     */
    private String subject = "南京晓庄学院";
    /**
     * 剩余天数
     */
    private Long licenseDays = 60L;
    /**
     * 剩余天数（根据当前时间动态计算）
     */
    private Long limitDays = 0L;
    /**
     * 系统运行天数
     */
    private Long onlineDays = 0L;
    /**
     * 剩余不足天数开始告警
     */
    private Integer warnDays = 15;

    private LocalDate buyTime = LocalDate.of(2020, Month.MAY, 30);
    /**
     * 是否免费
     */
    private Boolean isFree;

    private Boolean warning = Boolean.FALSE;
    private Boolean error = Boolean.FALSE;


}
