/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.common.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午8:45
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public class ExcelHandleUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static Workbook create(InputStream inputStream, String suffix) throws IOException {

        if ("xls".equals(suffix)) {
            return new HSSFWorkbook(inputStream);
        }

        if ("xlsx".equals(suffix)) {
            return new XSSFWorkbook(inputStream);
        }
        return null;
    }

    public static String getStringCellValue(Cell cell) {
        String strCell = "";

        if (cell != null) {

            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_FORMULA:
                    try {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            strCell = sdf.format(date);
                            break;
                        } else {
                            strCell = String.valueOf(cell.getNumericCellValue());
                        }

                    } catch (IllegalStateException e) {
                        strCell = String.valueOf(cell.getRichStringCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    strCell = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    strCell = cell.getStringCellValue();
                    //strCell = String.valueOf(cell.getNumericCellValue());
                    break;
            }
        }
        return strCell;
    }
}
