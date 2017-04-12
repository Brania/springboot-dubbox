/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.domain.dto.TransmitEventTreeNode;
import cn.zhangxd.platform.admin.web.service.TransmitEventService;
import lombok.extern.slf4j.Slf4j;
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
