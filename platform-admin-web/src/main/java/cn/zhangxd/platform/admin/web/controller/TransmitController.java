/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.domain.dto.TransmitEventTreeNode;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitRecordRequest;
import cn.zhangxd.platform.admin.web.security.model.AuthUser;
import cn.zhangxd.platform.admin.web.service.TransmitEventService;
import cn.zhangxd.platform.common.web.util.WebUtils;
import cn.zhangxd.platform.system.api.entity.AcKeyMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


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


    @GetMapping(value = "/list")
    public List<TransmitEventTreeNode> list() {
        return transmitEventService.getTransmitEventList();
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
        }else{
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
