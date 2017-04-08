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
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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

    @Autowired
    private StudentService studentService;


    @GetMapping(value = "/depart/list")
    public Page<Depart> listDepart(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE) int pageSize,
                                   @RequestParam Map<String, Object> searchParams) {


        return dictService.getDepartPages(searchParams, PaginationUtil.generate(page, pageSize));
    }


    @GetMapping(value = "/depart/{id}/detail")
    public Depart findDepartForm(@PathVariable Long id) {
        return dictService.findDepartById(id);
    }

    @PostMapping(value = "/depart/persist")
    public Depart saveOrUpdateDepart(@Valid @RequestBody Depart depart) {
        return dictService.persistDepart(depart);
    }

    @PostMapping(value = "/depart/{id}/delete")
    public Map<String, Object> removeDepart(@PathVariable Long id) {

        Depart depart = dictService.findDepartById(id);
        Map<String, Object> results = new HashedMap();
        String message = "";
        Boolean flag = Boolean.FALSE;
        if (null != depart) {
            Long count = studentService.countByDepart(depart);
            if (count > 0) {
                StringJoiner sj = new StringJoiner("");
                sj.add("共有").add(String.valueOf(count)).add("名学生归属于").add(depart.getName()).add("，您无法删除该院系!");
                message = sj.toString();
            } else {
                flag = dictService.deleteDepartById(id);
            }
        }
        results.put("success", flag);
        results.put("message", message);
        return results;
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
