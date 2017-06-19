/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.enums;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午12:40
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * 档案转接状态定义
 */
public enum TransmitEnum {
    /**
     * 入学::待接收->转入::已接收->转出::待接收
     * 晓庄转接事由逻辑::WAITING->仅转专业::转出一条记录
     * ## 修改枚举需要同步前端项目字典数据 ##
     */
    TRANSIENT("待接收"), ACCEPTED("已接收"), WAITING("转出待接收"), RECEIVED("已接收（转专业）"), DETACHED("毕业转出");

    private String name;

    private TransmitEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
