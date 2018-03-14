package cn.zhangxd.platform.admin.web.service.impl;

import cn.zhangxd.platform.admin.web.domain.Transform;
import cn.zhangxd.platform.admin.web.repository.TransformRepository;
import cn.zhangxd.platform.admin.web.security.model.AuthUser;
import cn.zhangxd.platform.admin.web.service.TransformService;
import cn.zhangxd.platform.common.web.util.WebUtils;
import cn.zhangxd.platform.system.api.entity.AcKeyMap;
import cn.zhangxd.platform.system.api.entity.SysUser;
import cn.zhangxd.platform.system.api.service.ISystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private ISystemService systemService;


    @Override
    public Transform findNotifyByLogin() {
        AuthUser authUser = WebUtils.getCurrentUser();
        return transformRepository.findByToMemberIdAndDone(authUser.getId(), Boolean.FALSE);
    }

    @Override
    public Boolean addTransformEvent(String memberId) {


        Transform transform = new Transform();
        transform.setToMemberId(memberId);

        AuthUser loginUser = WebUtils.getCurrentUser();
        transform.setFromMemberId(loginUser.getId());
        transform.setDone(Boolean.FALSE);
        transform.setCreateTime(new Date());

        AuthUser authUser = WebUtils.getCurrentUser();
        SysUser sysUser = systemService.getUserById(authUser.getId());
        Optional<AcKeyMap> deptOpt = sysUser.getDeparts().stream().findFirst();
        if (deptOpt.isPresent()) {
            AcKeyMap deptMap = deptOpt.get();
            transform.setDeptName(deptMap.getName());
            transform.setDeptCode(deptMap.getCode());
        }

        return transformRepository.save(transform) != null ? Boolean.TRUE : Boolean.FALSE;
    }


    /**
     * 1. 设置角色
     * 2. 添加管理院系
     * 3. 冻结发起方账号
     * 4. 设置交接流程表完成状态
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Boolean finishTransformEvent(String id) throws Exception {

        Transform transform = transformRepository.findOne(Long.parseLong(id));
        AuthUser auth = WebUtils.getCurrentUser();
        if (!auth.getId().equals(transform.getToMemberId())) {
            throw new Exception("非法的档案交接操作");
        }

        Boolean hasRole = systemService.handleFinishTransform(transform.getFromMemberId(), transform.getToMemberId());
        if (hasRole) {
            transform.setDone(Boolean.TRUE);
            transformRepository.save(transform);
        }
        return Boolean.TRUE;
    }


    @Override
    public List<Transform> listAllUndo() {
        return this.transformRepository.findProcessTransform();
    }
}
