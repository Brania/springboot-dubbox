/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service;

import cn.zhangxd.platform.admin.web.domain.Depart;
import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.TransmitEventType;
import cn.zhangxd.platform.admin.web.domain.TransmitRecord;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitEventTreeNode;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitRecordDto;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitRecordRequest;
import cn.zhangxd.platform.admin.web.enums.TransmitEventEnum;

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
public interface TransmitEventService {


    List<TransmitEventTreeNode> getTransmitEventList();

    TransmitEventTreeNode findById(Long id, Integer level);

    TransmitEventTreeNode persist(TransmitEventTreeNode transmitEventTreeNode);

    Map<String, Object> deleteById(Long id, Integer level);

    Map<String, Object> handleTransmitEvent(TransmitRecordRequest recordRequest);

    /**
     * 转专业::同意转入Boolean.TRUE| 转专业::拒绝转入Boolean.FALSE
     *
     * @param recordRequest
     * @return
     */
    Map<String, Object> handleAuditTransmitEvent(TransmitRecordRequest recordRequest, Boolean flag);

    /**
     * 办理入学
     *
     * @param student
     * @return
     */
    Boolean handleEnrollTransmitEvent(Student student);

    /**
     * 统计待接收档案数
     *
     * @return
     */
    Integer countArchiveToReceiveAmount(Integer statYear);

    /**
     * 统计转专业转出档案数
     *
     * @return
     */
    Integer countArchiveRollOutAmount(Integer statYear);

    /**
     * 统计转专业转入档案数
     *
     * @return
     */
    Integer countArchiveReceiveAmount(Integer statYear);

    /**
     * 按入学年份统计已接收档案数
     *
     * @param depart
     * @param type
     * @param statYear
     * @return
     */
    Long countArchiveAcceptedAmountByDepart(Depart depart, TransmitEventType type, Integer statYear);

    /**
     * 查询学生档案
     *
     * @param student
     * @param event
     * @return
     */
    List<TransmitRecordDto> findByEventTypeAndStudent(TransmitEventEnum event, Student student);


}
