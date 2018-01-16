/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service;

import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.common.ArchiveStat;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午7:33
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public interface DashboardService {

    /**
     * 按管理院系纬度统计待接收、已转出档案数量
     *
     * @return
     */
    Map<String, Integer> countArchiveAmount();


    /**
     * 查询首页转专业待办学生列表
     *
     * @return
     */
    List<Student> findTransmitTodoList();

    /**
     * 统计各院系档案转接数量
     *
     * @return
     */
    List<ArchiveStat> statArchiveAmountByDepart(Integer statYear);


}
