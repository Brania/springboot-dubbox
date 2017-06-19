/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.birt;

import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.StudentRelArchiveItem;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveItemDto;
import cn.zhangxd.platform.admin.web.service.ArchiveService;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.service.impl.ArchiveServiceImpl;
import cn.zhangxd.platform.admin.web.service.impl.StudentServiceImpl;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.Generator;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.IUpdatableDataSetRow;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.eventadapter.ScriptedDataSetEventAdapter;
import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午8:47
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description: 学生档案封面打印
 */
@Component
@Slf4j
public class NjxzcCoverDataSetEvent extends ScriptedDataSetEventAdapter {


    private Iterator<ArchiveItemDto> iter;


    @Override
    public boolean fetch(IDataSetInstance dataSet, IUpdatableDataSetRow row) throws ScriptException {


        if (iter.hasNext()) {

            ArchiveItemDto archiveItemDto = iter.next();
            row.setColumnValue("cname", archiveItemDto.getClassifyName());
            row.setColumnValue("name", archiveItemDto.getName());
            row.setColumnValue("amount", archiveItemDto.getFileExist() ? 1 : 0);
            row.setColumnValue("remark", archiveItemDto.getRemarks());

            if (null != archiveItemDto.getCreateTime()) {
                row.setColumnValue("year", Constants.SF_YEAR.format(archiveItemDto.getCreateTime()));
                row.setColumnValue("month", Constants.SF_MONTH.format(archiveItemDto.getCreateTime()));
                row.setColumnValue("day", Constants.SF_DAY.format(archiveItemDto.getCreateTime()));
            }
            return true;
        }
        return false;
    }

    @Override
    public void afterOpen(IDataSetInstance dataSet, IReportContext reportContext) throws ScriptException {

        StudentService studentService = ApplicationContextHolder.getContext().getBean(StudentServiceImpl.class);
        ArchiveService archiveService = ApplicationContextHolder.getContext().getBean(ArchiveServiceImpl.class);

        List<ArchiveItemDto> items = archiveService.findAll(new Sort(Sort.Direction.DESC, "sort"));

        Long sid = Long.parseLong(String.valueOf(reportContext.getParameterValue("sid")));
        Student student = studentService.findOne(sid);
        if (null != student) {
            List<StudentRelArchiveItem> sra = studentService.findArchiveItemByStudent(student);
            items = Generator.generate(items, sra);
        }

        iter = items.iterator();
    }
}
