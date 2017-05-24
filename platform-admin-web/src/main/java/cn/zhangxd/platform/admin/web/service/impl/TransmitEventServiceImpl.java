/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service.impl;

import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.TransmitEvent;
import cn.zhangxd.platform.admin.web.domain.TransmitEventType;
import cn.zhangxd.platform.admin.web.domain.TransmitRecord;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitEventTreeNode;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitRecordRequest;
import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import cn.zhangxd.platform.admin.web.repository.DepartRepository;
import cn.zhangxd.platform.admin.web.repository.TransmitEventRepository;
import cn.zhangxd.platform.admin.web.repository.TransmitEventTypeRepository;
import cn.zhangxd.platform.admin.web.repository.TransmitRecordRepository;
import cn.zhangxd.platform.admin.web.security.model.AuthUser;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.service.TransmitEventService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.common.web.util.WebUtils;
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
                    TransmitRecord transmitRecord = this.generate(recordRequest, student, transmitEventType, user);
                    transmitRecordRepository.save(transmitRecord);
                }
                // 不允许修改毕业转出学生档案状态
                if (!student.getStatus().equals(TransmitEnum.DETACHED)) {
                    if (student.getStatus().equals(TransmitEnum.TRANSIENT)) {
                        student.setStatus(TransmitEnum.ACCEPTED);
                    } else {
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
                    if (student.getStatus().equals(TransmitEnum.TRANSIENT)) {
                        student.setStatus(TransmitEnum.ACCEPTED);
                    } else {
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
                nodes.add(transmitEventType);
                transmitEvent.getChildren().add(transmitEventType);
            });
        });
        return nodes;
    }
}
