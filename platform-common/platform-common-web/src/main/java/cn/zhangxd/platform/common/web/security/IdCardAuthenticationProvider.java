package cn.zhangxd.platform.common.web.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2018/4/7
 */
@Component
public class IdCardAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Autowired
    public IdCardAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        IdCardAuthenticationToken token = (IdCardAuthenticationToken) authentication;
        UserDetails user = userDetailsService.loadUserByUsername(String.valueOf(token.getPrincipal()));
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        if (!StringUtils.equals(String.valueOf(token.getCredentials()), user.getPassword())) {
            throw new BadCredentialsException("身份证与学号不匹配");
        }
        // 已认证
        IdCardAuthenticationToken resultToken = new IdCardAuthenticationToken(user.getAuthorities(), token.getPrincipal(), token.getCredentials());
        resultToken.setDetails(user);

        return resultToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return IdCardAuthenticationToken.class.isAssignableFrom(authentication);
    }


}
