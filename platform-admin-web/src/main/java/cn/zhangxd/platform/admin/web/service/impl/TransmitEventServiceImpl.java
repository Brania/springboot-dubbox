/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service.impl;

import cn.zhangxd.platform.admin.web.domain.*;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitEventTreeNode;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitRecordDto;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitRecordRequest;
import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import cn.zhangxd.platform.admin.web.enums.TransmitEventEnum;
import cn.zhangxd.platform.admin.web.repository.DepartRepository;
import cn.zhangxd.platform.admin.web.repository.TransmitEventRepository;
import cn.zhangxd.platform.admin.web.repository.TransmitEventTypeRepository;
import cn.zhangxd.platform.admin.web.repository.TransmitRecordRepository;
import cn.zhangxd.platform.admin.web.security.model.AuthUser;
import cn.zhangxd.platform.admin.web.service.DictService;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.service.TransmitEventService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.Generator;
import cn.zhangxd.platform.admin.web.util.SecurityUtils;
import cn.zhangxd.platform.common.web.util.WebUtils;
import cn.zhangxd.platform.system.api.entity.AcKeyMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午1:01
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */

@Service
@Slf4j
public class TransmitEventServiceImpl implements TransmitEventService {

    @Autowired
    private TransmitEventRepository transmitEventRepository;
    @Autowired
    private TransmitEventTypeRepository transmitEventTypeRepository;
    @Autowired
    private TransmitRecordRepository transmitRecordRepository;
    @Autowired
    private DepartRepository departRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private DictService dictService;


    @Override
    public List<TransmitRecordDto> findByEventTypeAndStudent(TransmitEventEnum event, Student student) {

        List<TransmitEvent> events = transmitEventRepository.findByEvent(event);
        List<TransmitEventType> eventTypes = transmitEventTypeRepository.findByTransmitEventIn(events);
        return transmitRecordRepository.findByStudentAndTransmitEventTypeInOrderByFluctTimeDesc(student, eventTypes).stream().map(record -> Generator.generate(record)).collect(Collectors.toList());
    }

    @Override
    public Integer countArchiveRollOutAmountByDepart(Depart depart) {

        List<TransmitEventType> transmitEventTypes = transmitEventTypeRepository.findByNextStatus(TransmitEnum.WAITING);
        Integer departRollOutAmount = 0;
        if (null != transmitEventTypes && transmitEventTypes.size() > 0) {
            departRollOutAmount = transmitRecordRepository.countByTransmitEventTypeAndDepart(transmitEventTypes.get(0), depart).intValue();
        }
        return departRollOutAmount;
    }

    @Override
    public Integer countArchiveReceiveAmountByDepart(Depart depart) {
        List<TransmitEventType> transmitEventTypes = transmitEventTypeRepository.findByNextStatus(TransmitEnum.RECEIVED);
        Integer departReceivedAmount = 0;
        if (null != transmitEventTypes && transmitEventTypes.size() > 0) {
            departReceivedAmount = transmitRecordRepository.countByTransmitEventTypeAndDepart(transmitEventTypes.get(0), depart).intValue();
        }
        return departReceivedAmount;
    }

    @Override
    public Integer countArchiveRollOutAmount() {

        Integer rollOutAmount = 0;

        Depart depart = dictService.findCurrentUserDepart();

        List<TransmitEventType> transmitEventTypes = transmitEventTypeRepository.findByNextStatus(TransmitEnum.WAITING);
        if (null != transmitEventTypes && transmitEventTypes.size() > 0) {
            rollOutAmount = SecurityUtils.hasAdminRole() ? transmitRecordRepository.countByTransmitEventType(transmitEventTypes.get(0)).intValue() : transmitRecordRepository.countByTransmitEventTypeAndDepart(transmitEventTypes.get(0), depart).intValue();
        }
        return rollOutAmount;
    }

    @Override
    public Integer countArchiveReceiveAmount() {


        Integer receiveAmount = 0;

        Depart depart = dictService.findCurrentUserDepart();

        List<TransmitEventType> transmitEventTypes = transmitEventTypeRepository.findByNextStatus(TransmitEnum.RECEIVED);
        if (null != transmitEventTypes && transmitEventTypes.size() > 0) {
            receiveAmount = SecurityUtils.hasAdminRole() ? transmitRecordRepository.countByTransmitEventType(transmitEventTypes.get(0)).intValue() : transmitRecordRepository.countByTransmitEventTypeAndDepart(transmitEventTypes.get(0), depart).intValue();
        }

        return receiveAmount;
    }

    /**
     * 按照招就处或管理院系统计待接收档案数
     * 统计业务::待接收::新生待接收::转专业待接收
     *
     * @return
     */
    @Override
    public Integer countArchiveToReceiveAmount() {

        List<TransmitEnum> list = Lists.newArrayList();
        list.add(TransmitEnum.TRANSIENT);
        list.add(TransmitEnum.WAITING);
        Integer amount = 0;
        if (SecurityUtils.hasAdminRole()) {
            amount = studentService.findByStatusIn(list).size();
        } else {

            Depart depart = dictService.findCurrentUserDepart();
            if (null != depart) {
                amount = studentService.findByDepartAndStatusIn(depart, list).size();
            }
        }
        return amount;
    }


    @Override
    @Transactional
    public Boolean handleEnrollTransmitEvent(Student student) {

        Boolean flag = Boolean.FALSE;
        try {
            AuthUser user = WebUtils.getCurrentUser();
            // 1. 修改档案状态
            student.setStatus(TransmitEnum.ACCEPTED);

            TransmitRecord transmitRecord = new TransmitRecord();
            Optional<TransmitEvent> eventOptional = transmitEventRepository.findByEvent(TransmitEventEnum.NEW).stream().findFirst();

            if (eventOptional.isPresent()) {
                // 系统设置: 入学事由仅设置转入转接类型
                Optional<TransmitEventType> eventTypeOptional = eventOptional.get().getEventTypes().stream().findFirst();
                if (eventTypeOptional.isPresent()) {
                    transmitRecord.setTransmitEventType(eventTypeOptional.get());
                }
            }

            transmitRecord.setRemarks("入学档案");
            transmitRecord.setDepart(student.getDepart());
            transmitRecord.setFromDepart(student.getDepart());
            transmitRecord.setStudent(student);
            transmitRecord.setOpUserId(user.getId());
            transmitRecord.setOpUserName(user.getLoginName());
            // 入学保管人
            transmitRecord.setCustodian(student.getDepart().getName());
            transmitRecord.setFluctTime(new Date());
            transmitRecordRepository.save(transmitRecord);

            flag = Boolean.TRUE;

        } catch (Exception e) {
            log.error("办理入学档案失败：{}", e.getMessage());
        }
        return flag;
    }

    @Override
    @Transactional
    public Map<String, Object> handleAuditTransmitEvent(TransmitRecordRequest recordRequest, Boolean flag) {

        Boolean success = Boolean.TRUE;
        Map<String, Object> results = Maps.newHashMap();

        try {
            List<TransmitEventType> transmitEventTypes = transmitEventTypeRepository.findByNextStatus(TransmitEnum.RECEIVED);
            if (null != transmitEventTypes && transmitEventTypes.size() > 0) {
                TransmitEventType receivedEventType = transmitEventTypes.get(0);
                Student student = studentService.getStudentInfo(recordRequest.getSearchParam());
                if (null != student) {
                    if (flag) {
                        student.setDepart(student.getRollDepart());
                        student.setStatus(TransmitEnum.RECEIVED);
                    } else {
                        student.setRollDepart(null);
                        student.setStatus(TransmitEnum.ACCEPTED);
                    }
                    log.info("Student Status={}", student.getStatus().name());
                    AuthUser user = WebUtils.getCurrentUser();
                    TransmitRecord transmitRecord = this.generate(recordRequest, student, receivedEventType, user);
                    transmitRecordRepository.save(transmitRecord);
                }
            }
        } catch (Exception e) {
            success = Boolean.FALSE;
            log.error("拒绝接收档案操作失败::学生={}，错误信息={}", recordRequest.getSearchParam(), e.getMessage());
        }
        results.put("success", success);
        return results;
    }

    /**
     * 核心业务处理::转接办理
     *
     * @param recordRequest
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> handleTransmitEvent(TransmitRecordRequest recordRequest) {
        AuthUser user = WebUtils.getCurrentUser();

        Boolean success = Boolean.TRUE;
        Map<String, Object> results = Maps.newHashMap();

        try {
            TransmitEventType transmitEventType = transmitEventTypeRepository.findOne(Long.parseLong(recordRequest.getEventTypeId()));
            if (recordRequest.getSingle()) {
                // 单个处理
                Student student = studentService.getStudentInfo(recordRequest.getSearchParam());
                if (null != student) {
                    TransmitRecord transmitRecord;
                    // 防止重复提交转接数据
                    if (null != recordRequest.getRecId()) {
                        transmitRecord = transmitRecordRepository.findOne(recordRequest.getRecId());
                    } else {
                        transmitRecord = this.generate(recordRequest, student, transmitEventType, user);
                    }
                    transmitRecord = transmitRecordRepository.save(transmitRecord);
                    results.put("recId", transmitRecord.getId());
                }
                // 不允许修改毕业转出学生档案状态
                if (!student.getStatus().equals(TransmitEnum.DETACHED)) {
                    // 入学报到
                    if (student.getStatus().equals(TransmitEnum.TRANSIENT)) {
                        if (transmitEventType.getNextStatus().equals(TransmitEnum.ACCEPTED)) {
                            student.setStatus(TransmitEnum.ACCEPTED);
                        } else {
                            // 非法操作待接收学生
                            success = Boolean.FALSE;
                        }
                    } else {
                        // 在校学习::转专业
                        if (student.getStatus().equals(TransmitEnum.WAITING) && transmitEventType.getNextStatus().equals(TransmitEnum.RECEIVED)) {
                            // 认为是转入操作
                            student.setDepart(student.getRollDepart());
                        }
                        if (student.getStatus().equals(TransmitEnum.ACCEPTED)) {
                            // 认为是转出操作
                            student.setRollDepart(departRepository.findByCode(recordRequest.getToDepart()));
                        }
                        student.setStatus(transmitEventType.getNextStatus());
                    }
                }
            } else {
                // 批量处理
                Map<String, Object> searchMap = Maps.newHashMap();
                searchMap.put("depart", recordRequest.getDepart());
                List<Student> students = studentService.findStudentBySearchParam(searchMap);
                students.stream().map(s -> this.generate(recordRequest, s, transmitEventType, user)).forEach(record -> transmitRecordRepository.save(record));
                for (Student student : students) {
                    if (student.getStatus().equals(TransmitEnum.DETACHED)) {
                        break;
                    }
                    // 入学报到
                    if (student.getStatus().equals(TransmitEnum.TRANSIENT)) {
                        if (transmitEventType.getNextStatus().equals(TransmitEnum.ACCEPTED)) {
                            student.setStatus(TransmitEnum.ACCEPTED);
                        }
                        // 存在非法操作待接收学生记录丢弃
                    } else {
                        // 在校学习::转专业
                        if (student.getStatus().equals(TransmitEnum.WAITING) && transmitEventType.getNextStatus().equals(TransmitEnum.RECEIVED)) {
                            student.setDepart(student.getRollDepart());
                        }
                        if (student.getStatus().equals(TransmitEnum.ACCEPTED)) {
                            student.setRollDepart(departRepository.findByCode(recordRequest.getToDepart()));
                        }
                        student.setStatus(transmitEventType.getNextStatus());
                    }
                }
            }
        } catch (Exception e) {
            success = Boolean.FALSE;
            log.error("办理转接业务失败：{}", e.getMessage());
        }

        results.put("success", success);
        return results;
    }


    private TransmitRecord generate(TransmitRecordRequest recordRequest, Student student, TransmitEventType transmitEventType, AuthUser authUser) {
        Date currentTime = new Date();
        TransmitRecord transmitRecord = new TransmitRecord();
        transmitRecord.setCustodian(recordRequest.getCustodian());
        transmitRecord.setDepart(departRepository.findByCode(recordRequest.getToDepart()));
        List<AcKeyMap> acKeyMaps = authUser.getAccessPolicy();
        if (acKeyMaps.size() > 0) {
            transmitRecord.setFromDepart(departRepository.findByCode(acKeyMaps.get(0).getCode()));
        }
        transmitRecord.setOpUserId(authUser.getId());
        transmitRecord.setOpUserName(authUser.getLoginName());
        transmitRecord.setCreateTime(currentTime);
        transmitRecord.setStudent(student);
        transmitRecord.setRemarks(recordRequest.getRemarks());
        transmitRecord.setTransmitEventType(transmitEventType);
        transmitRecord.setFluctTime(currentTime);
        return transmitRecord;
    }

    @Override
    public Map<String, Object> deleteById(Long id, Integer level) {
        Map<String, Object> results = Maps.newHashMap();
        Boolean flag = Boolean.TRUE;
        try {
            if (level > 1) {
                transmitEventTypeRepository.delete(id);
            } else {
                transmitEventRepository.delete(id);
            }
        } catch (Exception e) {
            flag = Boolean.FALSE;
            log.error("删除转接事由失败：{}", e.getMessage());
        }

        results.put("success", flag);
        return results;
    }

    @Override
    public TransmitEventTreeNode persist(TransmitEventTreeNode transmitEventTreeNode) {

        if (StringUtils.isNotBlank(transmitEventTreeNode.getParentId())) {
            TransmitEvent transmitEvent = transmitEventRepository.findOne(Long.parseLong(transmitEventTreeNode.getParentId()));

            TransmitEventType transmitEventType;

            if (StringUtils.isNotBlank(transmitEventTreeNode.getId())) {
                transmitEventType = transmitEventTypeRepository.findOne(Long.parseLong(transmitEventTreeNode.getId()));
            } else {
                transmitEventType = new TransmitEventType();
            }
            transmitEventType.setName(transmitEventTreeNode.getName());
            transmitEventType.setCreateTime(new Date());
            transmitEventType.setEnabled(transmitEventTreeNode.getEnabled());
            transmitEventType.setTransmitEvent(transmitEvent);
            transmitEventType.setSort(transmitEventTreeNode.getSort());
            transmitEventType.setNextStatus(TransmitEnum.valueOf(transmitEventTreeNode.getStatus()));
            this.transmitEventTypeRepository.save(transmitEventType);

        } else {
            TransmitEvent transmitEvent;
            if (StringUtils.isNotBlank(transmitEventTreeNode.getId())) {
                transmitEvent = transmitEventRepository.findOne(Long.parseLong(transmitEventTreeNode.getId()));
            } else {
                transmitEvent = new TransmitEvent();
            }
            transmitEvent.setName(transmitEventTreeNode.getName());
            transmitEvent.setCreateTime(new Date());
            transmitEvent.setEvent(TransmitEventEnum.valueOf(transmitEventTreeNode.getEvent()));
            transmitEvent.setEnabled(transmitEventTreeNode.getEnabled());
            this.transmitEventRepository.save(transmitEvent);
        }
        return transmitEventTreeNode;
    }

    @Override
    public TransmitEventTreeNode findById(Long id, Integer level) {

        TransmitEventTreeNode transmitEventTreeNode = new TransmitEventTreeNode();

        if (level > 1) {

            TransmitEventType transmitEventType = this.transmitEventTypeRepository.findOne(id);
            transmitEventTreeNode.setId(String.valueOf(transmitEventType.getId()));
            transmitEventTreeNode.setName(transmitEventType.getName());
            transmitEventTreeNode.setEnabled(transmitEventType.getEnabled());
            transmitEventTreeNode.setSort(transmitEventType.getSort());
            transmitEventTreeNode.setStatus(transmitEventType.getNextStatus().name());
            transmitEventTreeNode.setParentId(String.valueOf(transmitEventType.getTransmitEvent().getId()));

        } else {
            TransmitEvent transmitEvent = this.transmitEventRepository.findOne(id);
            transmitEventTreeNode.setId(String.valueOf(transmitEvent.getId()));
            transmitEventTreeNode.setName(transmitEvent.getName());
            transmitEventTreeNode.setEvent(transmitEvent.getEvent().name());
            transmitEventTreeNode.setEnabled(transmitEvent.getEnabled());
        }
        return transmitEventTreeNode;
    }


    @Override
    public List<TransmitEventTreeNode> getTransmitEventList() {

        List<TransmitEventTreeNode> nodes = Lists.newArrayList();

        Iterable<TransmitEvent> events = transmitEventRepository.findAll();
        events.forEach(event -> {
            TransmitEventTreeNode transmitEvent = new TransmitEventTreeNode();
            transmitEvent.setId(String.valueOf(event.getId()));
            transmitEvent.setName(event.getName());
            transmitEvent.setEnabled(event.getEnabled());
            nodes.add(transmitEvent);

            event.getEventTypes().forEach(types -> {
                TransmitEventTreeNode transmitEventType = new TransmitEventTreeNode();
                transmitEventType.setId(Constants.TRANSMIT_TYPE_PREFIX + types.getId());
                transmitEventType.setName(types.getName());
                transmitEventType.setEnabled(types.getEnabled());
                transmitEventType.setParentId(String.valueOf(transmitEvent.getId()));
                transmitEventType.setStatus(types.getNextStatus().name());
                transmitEventType.setSort(types.getSort());

                nodes.add(transmitEventType);
                transmitEvent.getChildren().add(transmitEventType);
            });
        });
        return nodes;
    }
}
