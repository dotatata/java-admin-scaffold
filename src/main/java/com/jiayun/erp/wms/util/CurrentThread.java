package com.jiayun.erp.wms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CurrentThread {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    HttpServletRequest request;

    /**
     * 使用redis等缓存作为session管理时 获取用户
     * @return User
     */
    public com.jiayun.erp.wms.entity.User currentUserByRedis(){
        //redisUtil.get("user:");
        System.out.println(request.getHeader("x-token"));
        return new com.jiayun.erp.wms.entity.User();
    }

    /**
     * 此方法用于获取 JWT token中的user信息
     * @return User
     */
    //public static User currentJWT(){
    //
    //}

    /**
     * 此方法用于获取 使用SpringSecurity + 纯JWT时 在过滤器(本项目命名为AuthenticationTokenFilter)中置入user对象
     * @return
     */
    public static User currentUserBySecurityFilter(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                //System.out.println("((UserDetails) principal: " + principal);
                return (User) principal;
            }else {
                String username = principal.toString();
            }
        }else {
            return null;
        }
        return null;
    }
}
