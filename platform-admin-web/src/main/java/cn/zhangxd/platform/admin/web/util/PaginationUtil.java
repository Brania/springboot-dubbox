/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.util;

import cn.zhangxd.platform.common.api.Paging;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
     * @param pageNum
     * @param pageSize
     * @param sortType
     * @return
     */
    public static Paging generate(int pageNum, int pageSize, String sortType) {
        Paging paging = new Paging();
        paging.setPageNum(pageNum);
        paging.setPageSize(pageSize);
        paging.setOrderBy(sortType);
        return paging;
    }


    /**
     * 创建分页请求.
     */
    public static PageRequest buildPageRequest(int page, int pagzSize, String sortType) {
        Sort sort = new Sort(Sort.Direction.DESC, sortType);
        return new PageRequest(page - 1, pagzSize, sort);
    }


}
