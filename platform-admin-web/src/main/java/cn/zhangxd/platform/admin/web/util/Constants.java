/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.util;

import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午1:44
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public class Constants {


    public static final String PAGE_SIZE = "10";

    /**
     * 院系编码前缀
     */
    public static final String DEPART_CODE_PREFIX = "D";
    /**
     * 专业编码前缀
     */
    public static final String MAJOR_CODE_PREFIX = "M";
    /**
     * 行政班级编码前缀
     */
    public static final String ADCLASS_CODE_PREFIX = "C";

    /**
     * 默认排序规则::注意跨数据库问题
     */
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_TYPE = "ASC";

    public static final String TRANSMIT_TYPE_PREFIX = "_";
    public static final String DOT = ",";

    /**
     * 数据来源：区分导入数据和新增数据
     */
    public static final String IMPORT_SOURCE = "IMP";
    public static final String ADD_SOURCE = "ADD";


    /**
     * 报表单列::双列
     */
    public static final String DRT = "double";


    public static final SimpleDateFormat SF_YEAR = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat SF_MONTH = new SimpleDateFormat("MM");
    public static final SimpleDateFormat SF_DAY = new SimpleDateFormat("dd");


    public static final String LOG_TYPE_LOGIN = "系统登录";
    public static final String LOG_TYPE_TRANSMIT = "转接办理";

    public static final String NJXZC_CODE = "11460";


}
