/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.birt;

import cn.zhangxd.platform.admin.web.domain.Student;
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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午11:27
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Component
@Slf4j
public class NjzxcDataSetEventSlave extends ScriptedDataSetEventAdapter {

    private Iterator<Student> stuIter;

    private Collection<Student> studentCollection = Lists.newArrayList();


    @Override
    public boolean fetch(IDataSetInstance dataSet, IUpdatableDataSetRow row) throws ScriptException {
        if (stuIter.hasNext()) {
            Student student = stuIter.next();
            row.setColumnValue("eno", student.getExamineeNo());
            row.setColumnValue("ano", student.getAdmissionNo());
            row.setColumnValue("sno", student.getStudentNo());
            row.setColumnValue("name", student.getName());
            row.setColumnValue("depart", student.getDepart().getName());
            return true;
        }
        return false;
    }


    @Override
    public void afterOpen(IDataSetInstance dataSet, IReportContext reportContext) throws ScriptException {

        // 获取数据
        StudentService studentService = ApplicationContextHolder.getContext().getBean(StudentServiceImpl.class);

        NjzxcRequest njzxcRequest = JSON.parseObject(String.valueOf(reportContext.getParameterValue("ReportParamJson")), NjzxcRequest.class);
        if (StringUtils.isNotBlank(njzxcRequest.getStudents())) {
            List<Long> checkIds = Lists.newArrayList();
            for (String stuId : njzxcRequest.getStudents().split(Constants.DOT)) {
                checkIds.add(Long.parseLong(stuId));
            }
            if (Constants.DRT.equals(njzxcRequest.getReportType())) {
                // 双列显示
                //stuIter = studentService.reportChooseStudent(checkIds).stream().filter(student -> student.getId().intValue() % 2 == 0).collect(Collectors.toList()).iterator();
                int columnCount = 1;
                for (Student student : studentService.reportChooseStudent(checkIds)) {
                    if (columnCount % 2 == 0) {
                        studentCollection.add(student);
                    }
                    columnCount++;
                }
                stuIter = studentCollection.iterator();
            } else {
                // 单列显示
                stuIter = studentService.reportChooseStudent(checkIds).iterator();
            }
        } else {
            if (Constants.DRT.equals(njzxcRequest.getReportType())) {
                //stuIter = studentService.reportStudentBySearchMap(njzxcRequest.getSearchParams()).stream().filter(student -> student.getId().intValue() % 2 == 0).collect(Collectors.toList()).iterator();
                int columnCount = 1;
                for (Student student : studentService.reportStudentBySearchMap(njzxcRequest.getSearchParams())) {
                    if (columnCount % 2 == 0) {
                        studentCollection.add(student);
                    }
                    columnCount++;
                }
                stuIter = studentCollection.iterator();
            } else {
                stuIter = studentService.reportStudentBySearchMap(njzxcRequest.getSearchParams()).iterator();
            }
        }


    }
}
