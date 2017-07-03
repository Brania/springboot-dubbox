/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.birt.BIRTReport;
import cn.zhangxd.platform.admin.web.birt.NjzxcRequest;
import cn.zhangxd.platform.admin.web.birt.ReportRequest;
import cn.zhangxd.platform.admin.web.birt.ReportRunner;
import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.ExcelComponent;
import cn.zhangxd.platform.admin.web.util.Generator;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午7:14
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description: 打印条形码报表
 */
@RestController("ReportController")
@RequestMapping("/reports")
@Slf4j
public class ReportController {

    @Autowired
    private ReportRunner reportRunner;

    @Autowired
    private StudentService studentService;

    private static final String[] COLUMNS = {"ksh", "lqh", "xh", "xm", "xb", "mz", "sfzh", "zy", "yx", "bj", "lqnf", "txdz", "yzbm", "lxr", "lxdh1", "lxdh2", "dah", "sydq", "daqx", "jsdw", "ydh"};

    /**
     * 导出学生档案Excel表格
     *
     * @param reportRequest
     * @return
     */
    @PostMapping(value = "/xls")
    public ResponseEntity<byte[]> exportArchiveExcel(@RequestBody ReportRequest reportRequest,
                                                     HttpServletRequest request) throws IOException {
        List<Student> students;

        NjzxcRequest njzxcRequest = JSON.parseObject(reportRequest.getReportParameters(), NjzxcRequest.class);
        if (StringUtils.isNotBlank(njzxcRequest.getStudents())) {
            List<Long> checkIds = Lists.newArrayList();
            for (String stuId : njzxcRequest.getStudents().split(Constants.DOT)) {
                checkIds.add(Long.parseLong(stuId));
            }
            students = studentService.reportChooseStudent(checkIds);
        } else {
            students = studentService.reportStudentBySearchMap(njzxcRequest.getSearchParams());
        }


        Resource templateResource = new ClassPathResource("template.xls");
        Resource outResource = new ClassPathResource("out.xls");


        File template = templateResource.getFile();
        File target = outResource.getFile();


        List<Map<String, Object>> listData = students.stream().map(student -> {
            Map<String, Object> stuMap = Maps.newHashMap();
            stuMap.put(COLUMNS[0], student.getExamineeNo());
            stuMap.put(COLUMNS[1], student.getAdmissionNo());
            stuMap.put(COLUMNS[2], student.getStudentNo());
            stuMap.put(COLUMNS[3], student.getName());

            String sex = "";
            if (null != student.getSex()) {
                sex = student.getSex().getName();
            }
            stuMap.put(COLUMNS[4], sex);

            String nationality = "";
            if (null != student.getNationality()) {
                nationality = student.getNationality().getName();
            }
            stuMap.put(COLUMNS[5], nationality);

            stuMap.put(COLUMNS[6], student.getIdCard());

            String major = "";
            if (null != student.getMajor()) {
                major = student.getMajor().getName();
            }
            stuMap.put(COLUMNS[7], major);

            String depart = "";
            if (null != student.getDepart()) {
                depart = student.getDepart().getName();
            }
            stuMap.put(COLUMNS[8], depart);

            String clazz = "";
            if (null != student.getAdClass()) {
                clazz = student.getAdClass().getName();
            }
            stuMap.put(COLUMNS[9], clazz);

            stuMap.put(COLUMNS[10], student.getEntranceYear());
            stuMap.put(COLUMNS[11], student.getFamilyAddress());
            stuMap.put(COLUMNS[12], student.getPostCode());
            stuMap.put(COLUMNS[13], student.getLinkPerson());
            stuMap.put(COLUMNS[14], student.getPrimaryPhone());
            stuMap.put(COLUMNS[15], student.getBackupPhone());
            stuMap.put(COLUMNS[16], student.getArchiveNo());
            stuMap.put(COLUMNS[17], student.getSourceRegion());
            stuMap.put(COLUMNS[18], student.getArchiveGonePlace());
            stuMap.put(COLUMNS[19], student.getReceiveUnit());
            stuMap.put(COLUMNS[20], student.getTrackNo());
            return stuMap;
        }).collect(Collectors.toList());

        // 准备离散数据
        Map<String, Object> discreteData = Maps.newHashMap();

        Boolean isSuccess = Boolean.FALSE;
        byte[] res = new byte[0];
        try {
            isSuccess = ExcelComponent.generateSingleSheetExcelFile(
                    template, target, discreteData, listData, COLUMNS, "list0");
            if (isSuccess) {

                InputStream in = this.getClass().getResourceAsStream("/out.xls");
                if (null != in) {
                    res = IOUtils.toByteArray(in);
                }

            } else {
                log.error("Excel组件导出文件失败，请求数据 = {}", reportRequest.getReportParameters());
            }
        } catch (Exception e) {
            log.error("导出学生档案Excel表格出现异常：{}", e.getMessage());
        }

        String fileName = "out.xls";
        String header = request.getHeader("User-Agent").toUpperCase();
        HttpStatus status = HttpStatus.CREATED;

        if (isSuccess) {
            try {
                fileName = Generator.encodeFileName(header, fileName);
                status = HttpStatus.OK;
            } catch (UnsupportedEncodingException e) {
                log.error("下载文件响应失败：{}", e.getMessage());
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(res.length);

        return new ResponseEntity<>(res, headers, status);
    }

    /**
     * 导出档案条码
     *
     * @param reportRequest
     * @return
     */
    @PostMapping(value = "/birt")
    public ResponseEntity<byte[]> getBIRTReport(@RequestBody ReportRequest reportRequest) {
        byte[] reportBytes;
        ResponseEntity<byte[]> responseEntity;
        try {
            log.info("REPORT REQUEST NAME:   " + reportRequest.getReportName());
            reportBytes =
                    new BIRTReport(
                            reportRequest.getReportName(),
                            reportRequest.getReportParameters(),
                            reportRunner)
                            .runReport().getReportContent().toByteArray();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
            String fileName = reportRequest.getReportName() + ".pdf";
            httpHeaders.setContentDispositionFormData(fileName, fileName);
            httpHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            responseEntity = new ResponseEntity<>(reportBytes, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
            return responseEntity;
        }
        return responseEntity;
    }
}
