/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.task;

import cn.zhangxd.platform.admin.web.domain.dto.StudentXlsDto;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.util.AbstractExecutableTask;
import cn.zhangxd.platform.common.utils.ExcelHandleUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午8:51
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Slf4j
@Data
public class ImportStudentExcelTask extends AbstractExecutableTask {


    private MultipartFile excelFile;

    private StudentService studentService;

    private String suffix;

    @Override
    protected void runInternal() {

        InputStream inputStream = null;
        try {
            inputStream = excelFile.getInputStream();
            Workbook workBook = ExcelHandleUtil.create(inputStream, suffix);
            Sheet sheet = workBook.getSheetAt(0);

            if (sheet != null) {
                // 汇总数据
                List<StudentXlsDto> datas = new ArrayList<>();
                //log.info("计算行->getPhysicalNumberOfRows = {}", sheet.getPhysicalNumberOfRows());
                for (int i = sheet.getFirstRowNum() + 2; i < sheet.getPhysicalNumberOfRows(); i++) {
                    Row row = sheet.getRow(i);
                    StudentXlsDto dto = new StudentXlsDto();

                    //log.info("计算每一行列的数据->PhysicalNumberOfCells:{}", row.getPhysicalNumberOfCells());

                    //for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    // getFirstCellNum不等于0
                    for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            String cellStr = ExcelHandleUtil.getStringCellValue(cell);
                            // 跳过行数据为空的记录
                            if (j == 0 && StringUtils.isBlank(cellStr)) {
                                continue;
                            }
                            switch (j) {
                                // 考生号
                                case 0:
                                    dto.setKsh(cellStr);
                                    break;
                                // 录取号
                                case 1:
                                    dto.setLqh(cellStr);
                                    break;
                                // 学号
                                case 2:
                                    dto.setXh(cellStr);
                                    break;
                                // 姓名
                                case 3:
                                    dto.setXm(cellStr);
                                    break;
                                // 性别
                                case 4:
                                    dto.setXb(cellStr);
                                    break;
                                // 民族
                                case 5:
                                    dto.setMz(cellStr);
                                    break;
                                // 身份证
                                case 6:
                                    dto.setSfzh(cellStr);
                                    break;
                                // 专业
                                case 7:
                                    dto.setZy(cellStr);
                                    break;
                                // 院系
                                case 8:
                                    dto.setYx(cellStr);
                                    break;
                                // 班级
                                case 9:
                                    dto.setBj(cellStr);
                                    break;
                                // 录取年份
                                case 10:
                                    dto.setLqnf(cellStr);
                                    break;
                                // 通信地址
                                case 11:
                                    dto.setTxdz(cellStr);
                                    break;
                                // 邮编
                                case 12:
                                    dto.setYzbm(cellStr);
                                    break;
                                // 联系人
                                case 13:
                                    dto.setLxr(cellStr);
                                    break;
                                // 联系电话1
                                case 14:
                                    dto.setLxdh1(cellStr);
                                    break;
                                // 联系电话2
                                case 15:
                                    dto.setLxdh2(cellStr);
                                    break;
                                // 档案号
                                case 16:
                                    dto.setDah(cellStr);
                                    break;
                                //生源地区
                                case 17:
                                    dto.setSydq(cellStr);
                                    break;
                                // 档案去向
                                case 18:
                                    dto.setDaqx(cellStr);
                                    break;
                                // 档案接收单位
                                case 19:
                                    dto.setJsdw(cellStr);
                                    break;
                                // 运单号
                                case 20:
                                    dto.setYdh(cellStr);
                                    break;
                                // 标记
                                case 21:
                                    dto.setRemarks(cellStr);
                                    break;
                            }
                        } else {
                            continue;
                        }
                    }
                    datas.add(dto);
                }
                studentService.importStudent(datas);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行导入任务失败：{}", e.getMessage());
        }


    }
}
