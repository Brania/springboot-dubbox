/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service;

import cn.zhangxd.platform.admin.web.domain.TransmitEvent;
import cn.zhangxd.platform.admin.web.domain.TransmitEventType;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitEventTreeNode;

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


}
