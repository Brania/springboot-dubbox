/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.common.LogImpExcel;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.task.ImportStudentExcelTask;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午8:56
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */

@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/list")
    public Page<Student> list(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE) int pageSize,
                              @RequestParam(value = "sortType", defaultValue = "createTime") String sortType, @RequestParam Map<String, Object> searchParams) {

        return studentService.getStudentPages(searchParams, PaginationUtil.generate(page, pageSize, sortType));
    }


    @GetMapping(value = "/{id}/detail")
    public Student findById(@PathVariable Long id) {
        return studentService.findOne(id);
    }


    @PostMapping(value = "/persist")
    public Student saveOrUpdate(@Valid @RequestBody Student student) {
        return studentService.save(student);
    }

    /**
     * 执行导入学生数据
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/import/xls")
    public LogImpExcel importData(@RequestParam MultipartFile file) {

        LogImpExcel logImpExcel = new LogImpExcel();

        try {
            String originFileName = file.getOriginalFilename();
            String suffix = originFileName.substring(originFileName
                    .lastIndexOf(".") + 1);

            if ("xls".equals(suffix) || "xlsx".equals(suffix)) {
                ImportStudentExcelTask importStudentExcelTask = new ImportStudentExcelTask();
                importStudentExcelTask.setStudentService(studentService);
                importStudentExcelTask.setExcelFile(file);
                importStudentExcelTask.setSuffix(suffix);
                taskExecutor.execute(importStudentExcelTask);
            }
        } catch (Exception e) {
            log.error("执行导入学生名单失败：{}", e.getMessage());
        }
        return logImpExcel;
    }
}
