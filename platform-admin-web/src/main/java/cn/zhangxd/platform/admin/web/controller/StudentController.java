/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.StudentRelArchiveItem;
import cn.zhangxd.platform.admin.web.domain.common.LogImpExcel;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveItemDto;
import cn.zhangxd.platform.admin.web.domain.dto.StudentDetailDto;
import cn.zhangxd.platform.admin.web.domain.dto.StudentRequestForm;
import cn.zhangxd.platform.admin.web.service.ArchiveService;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.task.ImportStudentExcelTask;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.Generator;
import cn.zhangxd.platform.admin.web.util.PaginationUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
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

    @Autowired
    private ArchiveService archiveService;

    @GetMapping(value = "/list")
    public Page<Student> list(@RequestParam(value = "pageNum", defaultValue = "1") int page,
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
        if (studentService.countStudentTransmitTimes(id) > 0) {
            results.put("success", Boolean.FALSE);
            results.put("message", "删除失败，原因：不允许删除已存在转接记录的档案数据！");
        } else {
            results.put("success", studentService.deleteById(id));
        }
        return results;
    }


    @PostMapping(value = "/persist")
    public Student saveOrUpdate(@Valid @RequestBody StudentRequestForm student) {
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

    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> downloadXls(HttpServletRequest request) {

        byte[] res = new byte[0];
        try {
            InputStream in = this.getClass().getResourceAsStream("/out.xls");
            if (null != in) {
                res = IOUtils.toByteArray(in);
            }
        } catch (IOException e) {
            log.error("读取文件失败：{}", e.getMessage());
        }


        String fileName = "out.xls";
        String header = request.getHeader("User-Agent").toUpperCase();
        HttpStatus status = HttpStatus.CREATED;
        try {
            if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
                fileName = fileName.replace("+", "%20");    // IE下载文件名空格变+号问题
                status = HttpStatus.OK;
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("下载文件响应失败：{}", e.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(res.length);
//        log.info("Print File Size={}", res.length);

        return new ResponseEntity<>(res, headers, status);
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

    /**
     * 查询学生已添加档案列表
     *
     * @param sid
     * @return
     */
    @GetMapping(value = "/attach/find/{sid}")
    public List<ArchiveItemDto> queryArchiveAttachByStudent(@PathVariable Long sid) {

        List<ArchiveItemDto> items = archiveService.findAll(new Sort(Sort.Direction.DESC, "sort"));

        Student student = studentService.findOne(sid);
        if (null != student) {
            List<StudentRelArchiveItem> sra = studentService.findArchiveItemByStudent(student);
            items = Generator.generate(items, sra);
        }
        return items;
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
