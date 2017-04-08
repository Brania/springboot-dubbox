/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.birt;

import cn.zhangxd.platform.admin.web.service.StudentService;

import java.io.ByteArrayOutputStream;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午6:14
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public abstract class Report {

    protected String name;
    protected String parameters;
    protected ByteArrayOutputStream reportContent;
    protected ReportRunner reportRunner;

    public Report(String name, String parameters, ReportRunner reportRunner) {
        this.name = name;
        this.parameters = parameters;
        this.reportRunner = reportRunner;
    }

    /**
     * This is the processing method for a Report. Once the report is ran it
     * populates an internal field with a ByteArrayOutputStream of the
     * report content generated during the run process.
     *
     * @return Returns itself with the report content output stream created.
     */
    public abstract Report runReport();

    public ByteArrayOutputStream getReportContent() {
        return this.reportContent;
    }

    public String getName() {
        return name;
    }

    public String getParameters() {
        return parameters;
    }
}
