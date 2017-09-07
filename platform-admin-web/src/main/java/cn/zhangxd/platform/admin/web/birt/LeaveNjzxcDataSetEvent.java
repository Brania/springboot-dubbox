/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.birt;

import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.service.impl.StudentServiceImpl;
import cn.zhangxd.platform.admin.web.util.Constants;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.IUpdatableDataSetRow;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.eventadapter.ScriptedDataSetEventAdapter;
import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午5:39
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description: 导出毕业离校档案条码数据源
 */
@Component
@Slf4j
public class LeaveNjzxcDataSetEvent extends ScriptedDataSetEventAdapter {

    private Iterator<Student> stuIter;

    private Calendar calendar = Calendar.getInstance();

    // 默认离校报表数据源
    private String eventType;


    @Override
    public boolean fetch(IDataSetInstance dataSet, IUpdatableDataSetRow row) throws ScriptException {


        if (stuIter.hasNext()) {
            Student student = stuIter.next();
            String depart = "";
            if (null != student.getDepart()) {
                depart = student.getDepart().getName();
            }
            row.setColumnValue("depart", depart);
            row.setColumnValue("student_no", student.getStudentNo());
            String major = "";
            if (null != student.getMajor()) {
                major = student.getMajor().getName();
            }
            row.setColumnValue("major", major);
            row.setColumnValue("source_region", student.getSourceRegion());
            row.setColumnValue("archive_gone_place", student.getArchiveGonePlace());
            row.setColumnValue("archive_no", student.getArchiveNo());
            row.setColumnValue("student_name", student.getName());
            row.setColumnValue("track_no", student.getTrackNo());
            if (null != student.getAdClass()) {
                row.setColumnValue("adclass", student.getAdClass().getName());
            } else {
                row.setColumnValue("adclass", "");
            }
            String entranceYear = String.valueOf(student.getEntranceYear());
            if (StringUtils.equals(Constants.LEAVE_EVENT, eventType)) {
                // 打印毕业报表取毕业年份
                entranceYear = String.valueOf(calendar.get(Calendar.YEAR));
            }
            row.setColumnValue("barcode", entranceYear + Constants.NJXZC_CODE + student.getStudentNo());
            return true;
        }
        return false;
    }


    @Override
    public void afterOpen(IDataSetInstance dataSet, IReportContext reportContext) throws ScriptException {

        StudentService studentService = ApplicationContextHolder.getContext().getBean(StudentServiceImpl.class);
        NjzxcRequest njzxcRequest = JSON.parseObject(String.valueOf(reportContext.getParameterValue("ReportParamJson")), NjzxcRequest.class);
        if (StringUtils.isNotBlank(njzxcRequest.getStudents())) {
            List<Long> checkIds = Lists.newArrayList();
            for (String stuId : njzxcRequest.getStudents().split(Constants.DOT)) {
                checkIds.add(Long.parseLong(stuId));
            }
            stuIter = studentService.reportChooseStudent(checkIds).iterator();
        } else {
            stuIter = studentService.reportStudentBySearchMap(njzxcRequest.getSearchParams()).iterator();
        }

        eventType = njzxcRequest.getEventType();
    }
}
