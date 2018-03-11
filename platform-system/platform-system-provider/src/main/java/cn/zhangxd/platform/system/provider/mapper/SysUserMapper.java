package cn.zhangxd.platform.system.provider.mapper;


import cn.zhangxd.platform.common.service.dao.CrudDao;
import cn.zhangxd.platform.system.api.entity.AcKeyMap;
import cn.zhangxd.platform.system.api.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户DAO接口
 *
 * @author zhangxd
 */
@Mapper
public interface SysUserMapper extends CrudDao<SysUser> {

    /**
     * 根据登录名称查询用户
     *
     * @param loginName 登录名
     * @return SysUser by login name
     */
    SysUser getByLoginName(String loginName);

    /**
     * 查询用户访问策略
     *
     * @param userId
     * @return
     */
    List<AcKeyMap> getUserAccessPolicy(String userId);


    /**
     * 更新用户密码
     *
     * @param user the user
     * @return the int
     */
    int updatePasswordById(SysUser user);

    /**
     * 删除用户角色关联数据
     *
     * @param user the user
     * @return the int
     */
    int deleteUserRole(SysUser user);

    /**
     * 删除用户关联访问策略
     *
     * @param user
     * @return
     */
    int deleteUserAccessPolicy(SysUser user);

    /**
     * 插入用户角色关联数据
     *
     * @param user the user
     * @return the int
     */
    int insertUserRole(SysUser user);

    /**
     * 保存用户访问策略
     *
     * @param user
     * @return
     */
    int insertUserAccessPolicy(SysUser user);

    /**
     * 保存用户信息
     *
     * @param user the user
     */
    void updateInfo(SysUser user);

    /**
     * 查询可以将档案交接给对方的系统成员
     * @return
     */
    List<SysUser> queryPreTransformUsers();
}
