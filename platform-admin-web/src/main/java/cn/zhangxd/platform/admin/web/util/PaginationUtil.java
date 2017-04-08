/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.util;

import cn.zhangxd.platform.common.api.Paging;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午10:45
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public class PaginationUtil {

    /**
     * 创建分页请求对象
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static Paging generate(int pageNum, int pageSize) {
        Paging paging = new Paging();
        paging.setPageNum(pageNum);
        paging.setPageSize(pageSize);
        return paging;
    }


    /**
     * 创建分页请求.
     *
     * @param page
     * @param pagzSize
     * @param sortType 排序类型: ASC、DESC
     * @param sortBy   排序字段
     * @return
     */
    public static PageRequest buildPageRequest(int page, int pagzSize, String sortBy, String sortType) {
        Sort sort = new Sort("ASC".equals(sortType) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return new PageRequest(page - 1, pagzSize, sort);
    }


    public static String[] buildSortableParam(String orderParam) {

        String[] sortable = {Constants.DEFAULT_SORT_BY, Constants.DEFAULT_SORT_TYPE};

        if (StringUtils.isNotBlank(orderParam)) {
            sortable = orderParam.split(" ");
        }
        return sortable;

    }

    /**
     * 所有查询字段AND连接可以使用
     *
     * @param predicates
     * @param cb
     * @return
     */
    public static Predicate buildQueryPredicate(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[]{}));
    }


}
