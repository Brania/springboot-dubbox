/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 . All rights reserved.
 *          刀光剑影不是我门派，
 *          天空海阔自有我风采。
 *          双手一推非黑也非白，
 *          不好也不坏，没有胜又何来败。
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.enums;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: 2017/8/20
 * Time: 下午3:33
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description: 转接事由枚举::作用于转接事由定义
 */
public enum TransmitEventEnum {

    NEW("档案尚未到校"), PERSIST("在学校保管"), DETACHED("已毕业寄出");

    private String name;

    TransmitEventEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
