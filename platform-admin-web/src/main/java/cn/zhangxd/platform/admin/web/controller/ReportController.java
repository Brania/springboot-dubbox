/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.birt.BIRTReport;
import cn.zhangxd.platform.admin.web.birt.ReportRequest;
import cn.zhangxd.platform.admin.web.birt.ReportRunner;
import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


            log.info(";;;;;;---------------;;;;;;");

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
