/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.repository;

import cn.zhangxd.platform.admin.web.domain.TransmitEvent;
import cn.zhangxd.platform.admin.web.domain.TransmitEventType;
import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午3:18
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public interface TransmitEventTypeRepository extends PagingAndSortingRepository<TransmitEventType, Long>, JpaSpecificationExecutor<TransmitEventType> {

    List<TransmitEventType> findByNextStatus(TransmitEnum status);

    List<TransmitEventType> findByTransmitEventIn(List<TransmitEvent> events);

}
