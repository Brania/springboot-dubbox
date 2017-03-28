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
 * Time: 下午3:14
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */

public enum SexEnum {

    MALE("男"), FEMALE("女"), UNKNOW("未知");

    private String name;

    private SexEnum(String name) {
        this.name = name;
    }

    public static SexEnum findByName(String name) {
        for (SexEnum se : SexEnum.values()) {
            if (se.getName().equals(name)) {
                return se;
            }
        }
        return SexEnum.UNKNOW;
    }

    public String getName() {
        return name;
    }
}
