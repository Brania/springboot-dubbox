/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.domain.ArchiveItem;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveDto;
import cn.zhangxd.platform.admin.web.service.ArchiveService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.PaginationUtil;
import com.google.common.collect.Lists;
import javafx.scene.control.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    public List<ArchiveItem> list() {
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        List<ArchiveItem> list = Lists.newArrayList();
        for (Iterator<ArchiveItem> iterator = archiveService.findAll(sort).iterator(); iterator.hasNext(); ) {
            list.add(iterator.next());
        }
        return list;
    }

    @PostMapping(value = "/persist")
    public ArchiveItem saveOrUpdate(@Valid @RequestBody ArchiveItem archiveItem) {
        return archiveService.save(archiveItem);
    }

    @GetMapping(value = "/{id}/detail")
    public ArchiveItem getArchiveById(@PathVariable Long id) {
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


}
