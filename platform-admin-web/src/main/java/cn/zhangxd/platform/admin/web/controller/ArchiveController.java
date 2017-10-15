/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.domain.ArchiveClassify;
import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.StudentRelArchiveItem;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveClassifyDto;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveDto;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveItemDto;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitRecordDto;
import cn.zhangxd.platform.admin.web.enums.TransmitEventEnum;
import cn.zhangxd.platform.admin.web.service.ArchiveService;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.service.TransmitEventService;
import cn.zhangxd.platform.admin.web.util.CacheUtils;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.Generator;
import cn.zhangxd.platform.admin.web.util.PaginationUtil;
import cn.zhangxd.platform.common.web.annotations.Action;
import cn.zhangxd.platform.common.web.annotations.License;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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
    @Autowired
    private StudentService studentService;
    @Autowired
    private TransmitEventService transmitEventService;
    @Autowired
    private CacheUtils cacheUtils;


    @GetMapping(value = "/list")
    @License(action = Action.Check)
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
    @License(action = Action.Check)
    public Page<ArchiveDto> reportList(@RequestParam(value = "pageNum", defaultValue = "1") int page,
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

    @PostMapping(value = "/classify/{id}/delete")
    public Map<String, Object> deleteClassifyById(@PathVariable Long id) {
        return archiveService.deleteClassify(id);
    }

    /**
     * 档案查询人次
     *
     * @return
     */
    @GetMapping(value = "/fetch/times")
    public ResponseEntity<String> getArchiveQueryTimes() {
        return ResponseEntity.ok(String.valueOf(cacheUtils.getQueryRecordSize()));
    }

    /**
     * 通过学号、考生号查询学生档案
     *
     * @param keywords
     * @return
     */
    @GetMapping(value = "/search/{keywords}")
    public Map<String, Object> searchStudentArchive(@PathVariable String keywords) {


        StopWatch stopWatch = new StopWatch(ArchiveController.class.getName());
        stopWatch.start(Constants.ARCHIVE_QUERY_METHOD);

        Map<String, Object> results = Maps.newHashMap();


        Student student = studentService.getStudentArchiveByKeywords(keywords);
        if (null != student) {

            String sid = String.valueOf(student.getId());

            String nKey = String.format(Constants.STU_ARCHI_NEW_REC, sid);
            String pKey = String.format(Constants.STU_ARCHI_PERSIS_REC, sid);
            String dKey = String.format(Constants.STU_ARCHI_DETACHED_REC, sid);
            String aKey = String.format(Constants.STU_ARCHIVE_ITEMS, sid);

            results.put("success", Boolean.TRUE);

            // 基本信息
            results.put("student", student);
            // 到校转接记录
            if (cacheUtils.exists(nKey)) {
                log.info("学生={}档案查询;;;;;;加载缓存数据.", student.getName());
                results.put("newRecords", cacheUtils.findValues(nKey, TransmitRecordDto.class));
            } else {
                List<TransmitRecordDto> newRecDtos = transmitEventService.findByEventTypeAndStudent(TransmitEventEnum.NEW, student);
                cacheUtils.saveValues(nKey, JSON.toJSONString(newRecDtos), Constants.DAY);
                results.put("newRecords", newRecDtos);
            }

            // 校内转接记录
            if (cacheUtils.exists(pKey)) {
                results.put("persistRecords", cacheUtils.findValues(pKey, TransmitRecordDto.class));
            } else {
                List<TransmitRecordDto> perRecDtos = transmitEventService.findByEventTypeAndStudent(TransmitEventEnum.PERSIST, student);
                cacheUtils.saveValues(pKey, JSON.toJSONString(perRecDtos), Constants.DAY);
                results.put("persistRecords", perRecDtos);
            }

            // 离校转接记录
            if (cacheUtils.exists(dKey)) {
                results.put("detachedRecords", cacheUtils.findValues(dKey, TransmitRecordDto.class));
            } else {
                List<TransmitRecordDto> detRecDtos = transmitEventService.findByEventTypeAndStudent(TransmitEventEnum.DETACHED, student);
                cacheUtils.saveValues(dKey, JSON.toJSONString(detRecDtos), Constants.DAY);
                results.put("detachedRecords", detRecDtos);
            }

            // 档案内容
            if (cacheUtils.exists(aKey)) {
                results.put("archives", cacheUtils.findValues(aKey, ArchiveItemDto.class));
            } else {
                List<ArchiveItemDto> items = archiveService.findAll(new Sort(Sort.Direction.DESC, "sort"));
                List<StudentRelArchiveItem> sra = studentService.findArchiveItemByStudent(student);
                List<ArchiveItemDto> archiveItemDtos = Generator.generate(items, sra);
                results.put("archives", archiveItemDtos);
                cacheUtils.saveValues(aKey, JSON.toJSONString(archiveItemDtos), Constants.DAY);
            }
            // 记录档案查询次数
            if (cacheUtils.firstOps(sid)) {
                cacheUtils.pushRecord(sid);
            }
            // 档案查询状态转换
            String transStatus = Generator.convert(student.getStatus());

            results.put("transStatus", transStatus);
            results.put("transTimes", studentService.countStudentTransmitTimes(student.getId()));
        } else {
            results.put("success", Boolean.FALSE);
        }
        stopWatch.stop();
        log.info(String.format("学生 [ %s ] 档案查询 耗时 [ %f ]", keywords, stopWatch.getTotalTimeSeconds()));
        return results;
    }


}
