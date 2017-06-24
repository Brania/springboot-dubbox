package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.common.controller.BaseController;
import cn.zhangxd.platform.admin.web.domain.OperatorLog;
import cn.zhangxd.platform.admin.web.security.utils.TokenUtil;
import cn.zhangxd.platform.admin.web.service.OperatorLogService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.SecurityUtils;
import cn.zhangxd.platform.common.web.security.AuthenticationTokenFilter;
import cn.zhangxd.platform.system.api.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Authentication controller.
 *
 * @author zhangxd
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController extends BaseController {
    /**
     * 权限管理
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    /**
     * 用户信息服务
     */
    @Autowired
    private UserDetailsService userDetailsService;
    /**
     * Token工具
     */
    @Autowired
    private TokenUtil jwtTokenUtil;

    @Autowired
    private OperatorLogService operatorLogService;
    /**
     * Create authentication token bearer auth token.
     *
     * @param sysUser the sys user
     * @return the bearer auth token
     */
    @PostMapping(value = "/token")
    public Map<String, Object> createAuthenticationToken(@RequestBody SysUser sysUser, HttpServletRequest request) {

        // Perform the security
        // Return the token
        Map<String, Object> tokenMap = new HashMap<>();
        OperatorLog optLog = new OperatorLog();
        optLog.setUserName(sysUser.getLoginName());
        optLog.setOptType(Constants.LOG_TYPE_LOGIN);
        optLog.setCreateTime(new Date());
        optLog.setOptIp(SecurityUtils.getIpAddr(request));
        String loginInfo;
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(sysUser.getLoginName(), sysUser.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);


            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String token = jwtTokenUtil.generateToken(userDetails);

            loginInfo = String.format("用户%s登录了档案系统，认证结果为：%s", userDetails.getUsername(), Boolean.TRUE);


            tokenMap.put("access_token", token);
            tokenMap.put("expires_in", jwtTokenUtil.getExpiration());
            tokenMap.put("token_type", TokenUtil.TOKEN_TYPE_BEARER);
        } catch (Exception e) {
            loginInfo = String.format("用户%s登录了档案系统，认证结果为：%s", sysUser.getLoginName(), Boolean.FALSE);
            log.error("{}", e.getMessage());
        }
        optLog.setOptInfo(loginInfo);
        operatorLogService.saveOperatorLog(optLog);
        return tokenMap;


    }

    /**
     * Refresh and get authentication token bearer auth token.
     *
     * @param request the request
     * @return the bearer auth token
     */
    @GetMapping(value = "/refresh")
    public Map<String, Object> refreshAndGetAuthenticationToken(HttpServletRequest request) {

        String tokenHeader = request.getHeader(AuthenticationTokenFilter.TOKEN_HEADER);
        String token = tokenHeader.split(" ")[1];

        String username = jwtTokenUtil.getUsernameFromToken(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String refreshedToken = jwtTokenUtil.generateToken(userDetails);

        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("access_token", refreshedToken);
        tokenMap.put("expires_in", jwtTokenUtil.getExpiration());
        tokenMap.put("token_type", TokenUtil.TOKEN_TYPE_BEARER);
        return tokenMap;
    }

    /**
     * Delete authentication token response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @DeleteMapping(value = "/token")
    public ResponseEntity deleteAuthenticationToken(HttpServletRequest request) {

        String tokenHeader = request.getHeader(AuthenticationTokenFilter.TOKEN_HEADER);
        String token = tokenHeader.split(" ")[1];

        jwtTokenUtil.removeToken(token);

        return new ResponseEntity(HttpStatus.OK);
    }

}
