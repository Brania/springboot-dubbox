package cn.zhangxd.platform.common.web.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2018/4/7
 */
public class IdCardAuthenticationToken extends AbstractAuthenticationToken {


    private final Object principal;
    private Object credentials;


    public IdCardAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(Boolean.TRUE);
    }

    public IdCardAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setIdCardAuthenticated(Boolean.FALSE);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }


    public void setIdCardAuthenticated(boolean isAuthenticated) throws IllegalArgumentException{
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
