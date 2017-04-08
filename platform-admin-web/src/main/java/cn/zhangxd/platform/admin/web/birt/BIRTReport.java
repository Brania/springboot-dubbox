/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.birt;

import cn.zhangxd.platform.admin.web.service.StudentService;

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
public class BIRTReport extends Report {

    public BIRTReport(String name, String reportParameters, ReportRunner reportRunner) {
        super(name, reportParameters, reportRunner);
    }

    @Override
    public Report runReport() {
        this.reportContent = reportRunner.runReport(this);
        return this;
    }
}
