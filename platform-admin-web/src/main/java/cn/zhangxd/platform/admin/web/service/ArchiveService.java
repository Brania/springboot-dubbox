/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service;

import cn.zhangxd.platform.admin.web.domain.ArchiveItem;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveDto;
import cn.zhangxd.platform.common.api.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午4:43
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public interface ArchiveService {

    /**
     * 查找所有档案项目
     *
     * @param sort
     * @return
     */
    Iterable<ArchiveItem> findAll(Sort sort);

    /**
     * 保存或修改档案项目
     *
     * @param archiveItem
     * @return
     */
    ArchiveItem save(ArchiveItem archiveItem);

    ArchiveItem findById(Long id);

    /**
     * 删除档案项目
     *
     * @param id
     * @return
     */
    Map<String, Object> deleteArchiveItem(Long id);

    /**
     * 批量增加学生档案项目
     *
     * @param students
     * @param archiveId
     * @return
     */
    Map<String, Object> attachStuArchive(Stream<String> students, Long archiveId);

    /**
     * 批量删除学生档案项目
     *
     * @param students
     * @param archiveId
     * @return
     */
    Map<String, Object> deleteStuArchive(Stream<String> students, Long archiveId);

    /**
     * 分页显示档案内容管理或报表打印学生列表
     * @param searchParams
     * @param paging
     * @return
     */
    Page<ArchiveDto> reportPageList(Map<String, Object> searchParams, Paging paging);


}
