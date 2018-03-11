package cn.zhangxd.platform.admin.web.service;

import cn.zhangxd.platform.admin.web.domain.Transform;

import java.util.List;

/**
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2018/3/11
 */
public interface TransformService {

    /**
     * 发起档案交接事件
     *
     * @param memberId
     * @return
     */
    Boolean addTransformEvent(String memberId);

    /**
     * 完成档案交接
     *
     * @return
     */
    Boolean finishTransformEvent();


    List<Transform> listAllUndo();

    Boolean findNotifyByLogin();

}
