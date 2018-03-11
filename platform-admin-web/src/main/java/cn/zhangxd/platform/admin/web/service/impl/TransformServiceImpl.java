package cn.zhangxd.platform.admin.web.service.impl;

import cn.zhangxd.platform.admin.web.domain.Transform;
import cn.zhangxd.platform.admin.web.repository.TransformRepository;
import cn.zhangxd.platform.admin.web.security.model.AuthUser;
import cn.zhangxd.platform.admin.web.service.TransformService;
import cn.zhangxd.platform.common.web.util.WebUtils;
import cn.zhangxd.platform.system.api.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2018/3/11
 */
@Service
@Slf4j
public class TransformServiceImpl implements TransformService {

    @Autowired
    private TransformRepository transformRepository;


    @Override
    public Boolean findNotifyByLogin() {
        AuthUser authUser = WebUtils.getCurrentUser();
        return transformRepository.findByToMemberIdAndDone(authUser.getId(), Boolean.FALSE) != null ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public Boolean addTransformEvent(String memberId) {


        Transform transform = new Transform();
        transform.setToMemberId(memberId);

        AuthUser loginUser = WebUtils.getCurrentUser();
        transform.setFromMemberId(loginUser.getId());
        transform.setDone(Boolean.FALSE);
        transform.setCreateTime(new Date());

        return transformRepository.save(transform) != null ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public Boolean finishTransformEvent() {
        return null;
    }


    @Override
    public List<Transform> listAllUndo() {
        return this.transformRepository.findProcessTransform();
    }
}
