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
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.ExcelComponent;
import cn.zhangxd.platform.admin.web.util.Generator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
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
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

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


    /**
     * 导出学生档案Excel表格
     *
     * @param reportRequest
     * @return
     */
    @PostMapping(value = "/xls")
    public ResponseEntity<byte[]> exportArchiveExcel(@RequestBody ReportRequest reportRequest,
                                                     HttpServletRequest request) throws IOException {
        log.info("开始导出学生档案Excel表格..........");


        Resource templateResource = new ClassPathResource("template.xls");
        Resource outResource = new ClassPathResource("out.xls");


        File template = templateResource.getFile();
        File target = outResource.getFile();


        // 准备离散数据
        Map<String, Object> discreteData = Maps.newHashMap();
        discreteData.put("sumOfDog", 100);
        discreteData.put("sumOfCat", 130);
        // 准备列表数据
        List<Map<String, Object>> listData = Lists.newArrayList();
        Map<String, Object> map1 = Maps.newHashMap();
        map1.put("province", "广东省1");
        map1.put("county", "广州市1");
        map1.put("numOfDog", 30);
        map1.put("numOfCat", 40);
        listData.add(map1);
        Map<String, Object> map2 = Maps.newHashMap();
        map2.put("province", "广东省2");
        map2.put("county", "肇庆市2");
        map2.put("numOfDog", 50);
        map2.put("numOfCat", 60);
        listData.add(map2);
        Map<String, Object> map3 = Maps.newHashMap();
        map3.put("province", "江苏省3");
        map3.put("county", "东台市3");
        map3.put("numOfDog", 20);
        map3.put("numOfCat", 30);
        listData.add(map3);

        String[] columns = new String[]{"province", "county", "numOfDog",
                "numOfCat"};

        Boolean isSuccess = Boolean.FALSE;
        byte[] res = new byte[0];
        try {
            isSuccess = ExcelComponent.generateSingleSheetExcelFile(
                    template, target, discreteData, listData, columns, "list0");
            if (isSuccess) {

                InputStream in = this.getClass().getResourceAsStream("/out.xls");
                if (null != in) {
                    res = IOUtils.toByteArray(in);
                }

            } else {

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
