/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.domain.ArchiveItem;
import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.StudentRelArchiveItem;
import cn.zhangxd.platform.admin.web.domain.common.LogImpExcel;
import cn.zhangxd.platform.admin.web.domain.dto.StudentDetailDto;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.task.ImportStudentExcelTask;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.PaginationUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                              @RequestParam Map<String, Object> searchParams) {

        return studentService.getStudentPages(searchParams, PaginationUtil.generate(page, pageSize));
    }


    @GetMapping(value = "/{id}/detail")
    public Student findById(@PathVariable Long id) {
        return studentService.findOne(id);
    }

    @GetMapping(value = "/{id}/detailView")
    public StudentDetailDto findDetailById(@PathVariable Long id) {
        return studentService.findStudentDetail(id);
    }

    @GetMapping(value = "/{id}/delete")
    public Map<String, Object> delete(@PathVariable Long id) {

        Map<String, Object> results = Maps.newHashMap();
        results.put("success", studentService.deleteById(id));
        return results;
    }


    @PostMapping(value = "/persist")
    public Student saveOrUpdate(@Valid @RequestBody Student student) {
        return studentService.save(student);
    }

    /**
     * 考生号、姓名、身份证查询单个学生实体
     *
     * @param searchParam
     * @return
     */
    @GetMapping(value = "/transmit/search")
    public Student search(@RequestParam(value = "searchParam", defaultValue = "") String searchParam) {
        Student student = null;
        if (StringUtils.isNotBlank(searchParam)) {
            student = studentService.getStudentInfo(searchParam);
        }
        return student;
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


    @GetMapping(value = "/attach/find/{sid}")
    public List<ArchiveItem> queryArchiveAttachByStudent(@PathVariable Long sid) {

        List<ArchiveItem> list = Lists.newArrayList();
        Student student = studentService.findOne(sid);
        if (null != student) {
            List<StudentRelArchiveItem> sra = studentService.findArchiveItemByStudent(student);
            list = sra.stream().map(s -> s.getItem()).collect(Collectors.toList());
        }
        return list;
    }

    @GetMapping(value = "/attach/{sid}/delete/{id}")
    public Map<String, Object> deleteArchiveAttachById(@PathVariable Long sid, @PathVariable Long id) {
        Map<String, Object> results = Maps.newHashMap();
        Student student = studentService.findOne(sid);
        if (null != student) {
            results = studentService.deleteStudentAttachById(student, id);
        }
        return results;
    }
}
