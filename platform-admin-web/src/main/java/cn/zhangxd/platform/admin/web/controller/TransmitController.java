/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveOperatorDto;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitEventTreeNode;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitRecordRequest;
import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import cn.zhangxd.platform.admin.web.security.model.AuthUser;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.service.TransmitEventService;
import cn.zhangxd.platform.admin.web.util.CacheUtils;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.Generator;
import cn.zhangxd.platform.common.web.annotations.Action;
import cn.zhangxd.platform.common.web.annotations.License;
import cn.zhangxd.platform.common.web.util.WebUtils;
import cn.zhangxd.platform.system.api.entity.AcKeyMap;
import cn.zhangxd.platform.system.api.service.ISystemService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午12:58
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */

@RestController
@RequestMapping("/transmit")
@Slf4j
public class TransmitController {

    @Autowired
    private TransmitEventService transmitEventService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ISystemService systemService;

    private static final String ERROR = "办理失败: %s";
    // 仅显示最近十条记录
    private static final Integer ARCHIVE_TOTAL_INDEX = 10;


    @GetMapping(value = "/list")
    @License(action = Action.Check)
    public List<TransmitEventTreeNode> list() {
        return transmitEventService.getTransmitEventList();
    }

    @GetMapping(value = "/autocomplete/{code}")
    public Map<String, String> autocomplete(@PathVariable String code) {
        Map<String, String> results = Maps.newHashMap();
        results.put("dept_name", systemService.loadUsernameByAcKeyCode(code));
        return results;
    }

    /**
     * 保存或更新表单数据
     *
     * @param transmitEventTreeNode
     * @return
     */
    @PostMapping(value = "/persist")
    public TransmitEventTreeNode persistForm(@RequestBody TransmitEventTreeNode transmitEventTreeNode) {
        return transmitEventService.persist(transmitEventTreeNode);
    }

    @PostMapping(value = "/{id}/delete/{level}")
    public Map<String, Object> delete(@PathVariable Long id, @PathVariable Integer level) {
        return transmitEventService.deleteById(id, level);
    }

    /**
     * 执行转接业务操作
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/handle")
    public Map<String, Object> handleTransmitRecord(@RequestBody TransmitRecordRequest request) {
        return transmitEventService.handleTransmitEvent(request);
    }

    @GetMapping(value = "/enroll/list")
    @License(action = Action.Check)
    public Map<String, Object> viewTransmitByEnroll() {

        Map<String, Object> results = Maps.newHashMap();
        AuthUser authUser = WebUtils.getCurrentUser();
        String operKey = String.format(Constants.NJXZC_ENROLL_LIST, authUser.getId());
        List<String> students = cacheUtils.opsForHash().values(operKey);

        // 仅显示当天办理记录
        List<ArchiveOperatorDto> operatorDtoList = students.stream().map(s -> JSON.parseObject(s, ArchiveOperatorDto.class)).filter(archiveOperatorDto -> archiveOperatorDto.getBizTime().toLocalDate().isEqual(LocalDate.now())).sorted(Comparator.comparing(ArchiveOperatorDto::getBizTime).reversed()).collect(Collectors.toList());

        // 统计当天办理数量
        Long todayOperTimes = operatorDtoList.stream().filter(archiveOperatorDto -> archiveOperatorDto.getBizTime().toLocalDate().equals(LocalDate.now())).count();
        results.put("students", JSON.toJSONString(operatorDtoList));
        Long totalOperTimes = cacheUtils.opsForHash().size(operKey);
        results.put("totalOperTimes", totalOperTimes);
        results.put("todayOperTimes", todayOperTimes);
        return results;
    }


    /**
     * 办理入学档案
     *
     * @param keywords 支持条码及学号
     * @return
     */
    @PostMapping(value = "/enroll/{keywords}")
    public Map<String, Object> handleTransmitByEnroll(@PathVariable String keywords) {

        // 办理逻辑: 检查档案状态 -> 办理 -> 写缓存 -> 返回缓存列表
        Map<String, Object> results = Maps.newHashMap();
        Boolean result = Boolean.FALSE;

        Optional<Student> studentOptional = Optional.empty();
        if (StringUtils.isNotBlank(keywords)) {
            studentOptional = Optional.ofNullable(studentService.getStudentInfo(keywords));
        }

        // 兼容扫码枪自动查询
        if (!studentOptional.isPresent()) {
            studentOptional = Optional.ofNullable(studentService.getStudentInfoBarcode(keywords));
        }

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            if (student.getStatus().equals(TransmitEnum.TRANSIENT)) {
                // 入学档案业务办理
                result = transmitEventService.handleEnrollTransmitEvent(student);
            } else {
                results.put("message", String.format(ERROR, "非入学档案，不能重复办理入学"));
            }
            // 操作缓存
            String listKey = String.format(Constants.ENROLL_KEY, student.getStudentNo());
            AuthUser operAuthUser = WebUtils.getCurrentUser();
            String operKey = String.format(Constants.NJXZC_ENROLL_LIST, operAuthUser.getId());
            cacheUtils.opsForHash().put(operKey, listKey, JSON.toJSONString(Generator.generate(student)));
            results.put("students", cacheUtils.opsForHash().values(operKey));
            results.put("totalOperTimes", cacheUtils.opsForHash().size(operKey));
        } else {
            results.put("message", String.format(ERROR, "无效条码"));
        }
        results.put("success", result);
        return results;
    }


    /**
     * ##快捷办理转接业务##
     * 同意办理逻辑::转接类型NextStatus区分新生转入、转专业转入类型
     * 保存转接记录，修改Student中Depart字段。
     * 拒绝办理逻辑::置空Student中的RollDepart字段，插入转接记录并标注拒绝
     *
     * @return
     */
    @PostMapping(value = "/audit/handle")
    public Map<String, Object> dashboardHandleTransmitRecord(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> resultMap = Maps.newHashMap();

        String searchParam = String.valueOf(requestMap.get("searchParam"));
        String auditStatus = String.valueOf(requestMap.get("status"));
        if (StringUtils.isNotBlank(searchParam) && StringUtils.isNotBlank(auditStatus)) {
            Boolean flag = Boolean.TRUE;
            TransmitRecordRequest request = new TransmitRecordRequest();
            AuthUser authUser = WebUtils.getCurrentUser();
            request.setCustodian(authUser.getUsername());
            List<AcKeyMap> acKeyMaps = authUser.getAccessPolicy();
            if (acKeyMaps.size() > 0) {
                request.setToDepart(acKeyMaps.get(0).getCode());
            }
            request.setSingle(Boolean.TRUE);
            request.setSearchParam(searchParam);
            // 同意、拒绝
            if ("passed".equals(auditStatus)) {
                request.setRemarks("同意转入。");
            } else {
                flag = Boolean.FALSE;
                request.setRemarks("不同意转入。");
            }
            resultMap = transmitEventService.handleAuditTransmitEvent(request, flag);
        } else {
            requestMap.put("success", Boolean.FALSE);
        }
        return resultMap;
    }

    /**
     * 查询表单数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}/detail")
    public TransmitEventTreeNode findForm(@PathVariable Long id, @RequestParam Map<String, Object> searchParams) {
        Integer level = 1;
        if (null != searchParams.get("level")) {
            level = Integer.parseInt(String.valueOf(searchParams.get("level")));
        }
        return transmitEventService.findById(id, level);
    }


}
