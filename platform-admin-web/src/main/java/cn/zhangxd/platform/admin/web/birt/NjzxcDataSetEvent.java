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
import lombok.extern.slf4j.Slf4j;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.IUpdatableDataSetRow;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.eventadapter.ScriptedDataSetEventAdapter;
import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 上午12:00
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */

@Component
@Slf4j
public class NjzxcDataSetEvent extends ScriptedDataSetEventAdapter {


    private Iterator<Student> stuIter;

    @Override
    public boolean fetch(IDataSetInstance dataSet, IUpdatableDataSetRow row) throws ScriptException {

        if (stuIter.hasNext()) {
            Student student = stuIter.next();
            row.setColumnValue("examinee_no", student.getExamineeNo());
            row.setColumnValue("admission_no", student.getAdmissionNo());
            row.setColumnValue("student_no", student.getStudentNo());
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
        stuIter = studentService.findAll().iterator();
        // 设置报表参数
        // reportContext.setPageVariable("printers", "晓庄学院校印刷厂");
    }

}
