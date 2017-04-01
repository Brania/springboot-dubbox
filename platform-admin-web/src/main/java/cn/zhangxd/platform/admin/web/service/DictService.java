/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service;

import cn.zhangxd.platform.admin.web.domain.Depart;
import cn.zhangxd.platform.common.api.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午10:34
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public interface DictService {

    /**
     * 分页查询院系列表
     * @param searchParams
     * @param paging
     * @return
     */
    Page<Depart> getDepartPages(final Map<String, Object> searchParams, Paging paging);

    /**
     * 查询所有院系列表
     * @param sort
     * @return
     */
    Iterable<Depart> findAll(Sort sort);
}
