/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.repository;

import cn.zhangxd.platform.admin.web.domain.ArchiveClassify;
import cn.zhangxd.platform.admin.web.domain.ArchiveItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午4:39
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public interface ArchiveItemRepository extends PagingAndSortingRepository<ArchiveItem, Long>, JpaSpecificationExecutor<ArchiveItem> {


    Long countByForcedAndEnabled(Boolean forced, Boolean enabled);

    Long countByClassify(ArchiveClassify archiveClassify);

    @Query("select item from ArchiveItem item order by item.classify.id, item.sort asc")
    List<ArchiveItem> listAll();

}
