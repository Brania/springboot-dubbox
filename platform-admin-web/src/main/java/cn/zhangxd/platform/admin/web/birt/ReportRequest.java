/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.birt;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午6:16
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public class ReportRequest {

    private String reportName;
    private String reportParameters;

    public ReportRequest(@JsonProperty("reportName") String reportName,
                         @JsonProperty("reportParameters") String reportParameters) {
        this.reportName = reportName;
        this.reportParameters = reportParameters;
    }


    public String getReportName() {
        return reportName;
    }

    public String getReportParameters() {
        return reportParameters;
    }
}
