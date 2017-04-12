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

    TRANSIENT("未接收"), ACCEPTED("已接收"), WAITING("待接收"), DETACHED("已转出");

    private String name;

    private TransmitEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
