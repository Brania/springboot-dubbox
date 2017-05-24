/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.domain.ArchiveClassify;
import cn.zhangxd.platform.admin.web.domain.ArchiveItem;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveClassifyDto;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveDto;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveItemDto;
import cn.zhangxd.platform.admin.web.service.ArchiveService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.PaginationUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.scene.control.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午4:42
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@RestController
@RequestMapping("/archive")
@Slf4j
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    @GetMapping(value = "/list")
    public List<ArchiveItemDto> list() {
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        List<ArchiveItemDto> list = Lists.newArrayList();
        for (Iterator<ArchiveItemDto> iterator = archiveService.findAll(sort).iterator(); iterator.hasNext(); ) {
            list.add(iterator.next());
        }
        return list;
    }

    @GetMapping(value = "/list/classify")
    public List<ArchiveClassifyDto> listView() {
        Sort sort = new Sort(Sort.Direction.ASC, "createTime");
        return archiveService.findAllClassify(sort);
    }

    @PostMapping(value = "/persist")
    public ArchiveItemDto saveOrUpdate(@Valid @RequestBody ArchiveItemDto dto) {
        return archiveService.save(dto);
    }

    @GetMapping(value = "/{id}/detail")
    public ArchiveItemDto getArchiveById(@PathVariable Long id) {
        return archiveService.findById(id);
    }

    @PostMapping(value = "/{id}/delete")
    public Map<String, Object> deleteById(@PathVariable Long id) {
        return archiveService.deleteArchiveItem(id);
    }


    @GetMapping(value = "/report/list")
    public Page<ArchiveDto> reportList(@RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE) int pageSize,
                                       @RequestParam Map<String, Object> searchParams) {
        return archiveService.reportPageList(searchParams, PaginationUtil.generate(page, pageSize));
    }

    /**
     * 学生添加档案项目
     *
     * @param id
     * @param params
     * @return
     */
    @PostMapping(value = "/attach/{id}/add")
    public Map<String, Object> attachArchive(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        Map<String, Object> results = Maps.newHashMap();
        String stuIds = String.valueOf(params.get("stuIds"));
        List<String> stuList = Lists.newArrayList();
        if (StringUtils.isNotBlank(stuIds)) {
            StringTokenizer stringTokenizer = new StringTokenizer(stuIds, Constants.DOT);
            while (stringTokenizer.hasMoreElements()) {
                stuList.add(stringTokenizer.nextToken());
            }
            results = archiveService.attachStuArchive(stuList.stream(), id);
        }
        return results;
    }


    @PostMapping(value = "/classify/persist")
    public ArchiveClassify saveOrUpdate(@Valid @RequestBody ArchiveClassify archiveClassify) {
        return archiveService.save(archiveClassify);
    }

    @GetMapping(value = "/classify/{id}/detail")
    public ArchiveClassify getClassifyById(@PathVariable Long id) {
        return archiveService.findClassifyById(id);
    }


}
