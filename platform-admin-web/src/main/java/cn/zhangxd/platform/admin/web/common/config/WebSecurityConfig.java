package cn.zhangxd.platform.admin.web.common.config;

import cn.zhangxd.platform.common.web.config.AbstractWebSecurityConfig;
import cn.zhangxd.platform.common.web.util.SecurityConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * spring-security配置
 *
 * @author zhangxd
 */
@Configuration
public class WebSecurityConfig extends AbstractWebSecurityConfig {

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.cors().and()
                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/auth/token")
                .antMatchers("/auth/token", SecurityConstants.DACX_AUTH_URL)
                .permitAll();
        super.configure(security);
    }


}
