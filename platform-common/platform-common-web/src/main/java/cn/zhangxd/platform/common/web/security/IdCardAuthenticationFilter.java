package cn.zhangxd.platform.common.web.security;

import cn.zhangxd.platform.common.web.util.SecurityConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 身份证校验过滤器
 *
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2018/4/7
 */
public class IdCardAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * post请求
     */
    private boolean postOnly = true;

    public IdCardAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstants.DACX_AUTH_URL, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String idCard = request.getParameter("idcard");
        String sno = request.getParameter("sno");
        if (StringUtils.isNotEmpty(idCard) && StringUtils.isNotEmpty(sno)) {
            IdCardAuthenticationToken token = new IdCardAuthenticationToken(idCard, sno);
            // 设置用户信息
            setDetails(request, token);
            // 返回Authentication实例
            return this.getAuthenticationManager().authenticate(token);
        }
        return null;
    }

    protected void setDetails(HttpServletRequest request, IdCardAuthenticationToken token) {
        token.setDetails(authenticationDetailsSource.buildDetails(request));
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }
}
