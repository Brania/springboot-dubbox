/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.repository;

import cn.zhangxd.platform.admin.web.domain.TransmitRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午11:03
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public interface TransmitRecordRepository extends PagingAndSortingRepository<TransmitRecord, Long>, JpaSpecificationExecutor<TransmitRecord> {
}
