package cn.zhangxd.platform.common.web.annotations;

import java.lang.annotation.*;

/**
 *
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2017/10/15
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface License {

    Action action() default Action.Skip;
}
