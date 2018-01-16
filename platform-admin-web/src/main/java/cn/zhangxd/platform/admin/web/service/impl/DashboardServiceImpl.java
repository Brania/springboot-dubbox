/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service.impl;

import cn.zhangxd.platform.admin.web.domain.Depart;
import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.TransmitEventType;
import cn.zhangxd.platform.admin.web.domain.common.ArchiveStat;
import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import cn.zhangxd.platform.admin.web.repository.DepartRepository;
import cn.zhangxd.platform.admin.web.repository.TransmitEventTypeRepository;
import cn.zhangxd.platform.admin.web.security.model.AuthUser;
import cn.zhangxd.platform.admin.web.service.DashboardService;
import cn.zhangxd.platform.admin.web.service.DictService;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.service.TransmitEventService;
import cn.zhangxd.platform.admin.web.util.SecurityUtils;
import cn.zhangxd.platform.common.web.util.WebUtils;
import cn.zhangxd.platform.system.api.entity.AcKeyMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午7:50
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private TransmitEventService transmitEventService;
    @Autowired
    private TransmitEventTypeRepository transmitEventTypeRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private DepartRepository departRepository;
    @Autowired
    private DictService dictService;


    /**
     * 计算:档案总数::待接收::转出::转入档案数
     *
     * @return
     */
    @Override
    public Map<String, Integer> countArchiveAmount() {
        Map<String, Integer> archiveMap = Maps.newHashMap();
        if (SecurityUtils.hasAdminRole()) {
            archiveMap.put("totalCount", studentService.countTotalArchive().intValue());
        } else {
            archiveMap.put("totalCount", studentService.countArchiveByDepart(dictService.findCurrentUserDepart()).intValue());
        }
        // 待接收
        archiveMap.put("toRecCount", transmitEventService.countArchiveToReceiveAmount());
        // 转出
        archiveMap.put("rollOutCount", transmitEventService.countArchiveRollOutAmount());
        // 转入
        archiveMap.put("receiveCount", transmitEventService.countArchiveReceiveAmount());

        return archiveMap;
    }


    @Override
    public List<Student> findTransmitTodoList() {

        List<Student> students = Lists.newArrayList();
        if (!SecurityUtils.hasAdminRole()) {
            AuthUser authUser = WebUtils.getCurrentUser();
            List<AcKeyMap> acKeyMaps = authUser.getAccessPolicy();
            if (acKeyMaps.size() > 0) {
                Depart depart = departRepository.findByCode(acKeyMaps.get(0).getCode());
                students = studentService.findByRollDepartAndStatus(depart, TransmitEnum.WAITING);
            }
        }
        return students;
    }

    @Override
    public List<ArchiveStat> statArchiveAmountByDepart(Integer statYear) {

        List<ArchiveStat> archiveStats = studentService.statisticsStudentsGroupByDepart(statYear);
        // 统计各院系已接收档案数及未接收档案数
        return archiveStats.stream().map(archiveStat -> {
            Depart depart = departRepository.findByCode(archiveStat.getDepartCode());

            Optional<TransmitEventType> eventTypeOptional = transmitEventTypeRepository.findByNextStatus(TransmitEnum.ACCEPTED).stream().findFirst();
            Integer acceptCount = 0;
            if (eventTypeOptional.isPresent()) {
                acceptCount = transmitEventService.countArchiveAcceptedAmountByDepart(depart, eventTypeOptional.get()).intValue();
            }

            archiveStat.setAcceptedAmount(acceptCount);
            archiveStat.setWaitingAmount(archiveStat.getTotalAmount() - acceptCount);
            archiveStat.setDepartName(depart.getName());
            return archiveStat;
        }).collect(Collectors.toList());
    }
}
