package cn.zhangxd.platform.common.web.annotations;

/**
 * 授权拦截动作
 *
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2017/10/15
 */
public enum Action {

    Check("0", "授权检查"), Skip("1", "跳过权限验证");

    /**
     * 主键
     */
    private final String key;

    /**
     * 描述
     */
    private final String desc;


    Action(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
