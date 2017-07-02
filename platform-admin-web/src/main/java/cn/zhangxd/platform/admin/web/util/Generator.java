/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.util;

import cn.zhangxd.platform.admin.web.domain.StudentRelArchiveItem;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveItemDto;
import org.springframework.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午9:59
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public class Generator {


    public static String encodeFileName(String header, String fileName) throws UnsupportedEncodingException {

        if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = fileName.replace("+", "%20");    // IE下载文件名空格变+号问题

        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }

        System.out.println("----------------->> fileName="+ fileName);

        return fileName;

    }


    public static List<ArchiveItemDto> generate(List<ArchiveItemDto> items, List<StudentRelArchiveItem> sra) {
        return items.stream().map(archiveItemDto -> {
            for (StudentRelArchiveItem studentRelArchiveItem : sra) {
                if (studentRelArchiveItem.getItem().getId().compareTo(archiveItemDto.getId()) == 0) {
                    archiveItemDto.setFileExist(Boolean.TRUE);
                    archiveItemDto.setCreateTime(studentRelArchiveItem.getCreateTime());
                }
            }
            return archiveItemDto;
        }).collect(Collectors.toList());
    }
}