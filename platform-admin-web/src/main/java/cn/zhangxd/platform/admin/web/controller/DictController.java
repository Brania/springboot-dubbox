/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.domain.Depart;
import cn.zhangxd.platform.admin.web.service.DictService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午10:31
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description: 字典数据管理
 */
@RestController
@RequestMapping("/dict")
@Slf4j
public class DictController {

    @Autowired
    private DictService dictService;

    @GetMapping(value = "/depart/list")
    public Page<Depart> listDepart(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE) int pageSize,
                                   @RequestParam(value = "sortType", defaultValue = "createTime") String sortType, @RequestParam Map<String, Object> searchParams) {


        return dictService.getDepartPages(searchParams, PaginationUtil.generate(page, pageSize, sortType));
    }

    @GetMapping(value = "/depart/listAll")
    public List<Depart> findAll() {
        List<Depart> departs = new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.DESC, "code");
        for (Iterator<Depart> iter = this.dictService.findAll(sort).iterator(); iter.hasNext(); ) {
            departs.add(iter.next());
        }
        return departs;
    }


}
