package cn.zhangxd.platform.admin.web.security;

import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.security.model.AuthUser;
import cn.zhangxd.platform.admin.web.security.model.AuthUserFactory;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.system.api.entity.SysUser;
import cn.zhangxd.platform.system.api.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Security User Detail Service
 *
 * @author zhangxd
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    /**
     * 系统服务
     */
    @Autowired
    private ISystemService systemService;
    @Autowired
    private StudentService studentService;

    @Override
    public UserDetails loadUserByUsername(String loginName) {
        SysUser user = systemService.getUserByLoginName(loginName);

        if (user == null) {
            // TODO: 新增逻辑，根据学生档案身份证后六位查询
            Student student = studentService.getStudentByIdCard(loginName);
            // 构造学生授权对象
            if (null != student) {
                AuthUser authUser = new AuthUser(String.valueOf(student.getId()));
                authUser.setLoginName(student.getName());
                // 档案查询将学号作为密码校验
                authUser.setPassword(student.getStudentNo());
                authUser.setName(student.getName());
                authUser.setEnabled(Boolean.TRUE);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authUser.setAuthorities(authorities);
                return authUser;
            } else {
                throw new UsernameNotFoundException(String.format("No user found with username '%s'.", loginName));
            }
        } else {
            // platform用户
            return AuthUserFactory.create(user);
        }
    }
}
