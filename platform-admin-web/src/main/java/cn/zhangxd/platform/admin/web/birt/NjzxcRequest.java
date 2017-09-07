/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.birt;

import com.google.common.collect.Maps;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午10:20
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Data
public class NjzxcRequest implements Serializable {

    // 报表类型::单列::双列
    private String reportType;
    // 报表数据::0入学::1毕业
    private String eventType;

    private String students;

    private Map<String, String> searchParams = Maps.newHashMap();


}
