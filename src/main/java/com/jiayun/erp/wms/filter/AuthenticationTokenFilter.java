package com.jiayun.erp.wms.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiayun.erp.wms.util.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        response.setContentType("application/json;charset=utf-8");
        // 登录接口不校验token
        if(request.getRequestURI().startsWith("/auth/login") || request.getRequestURI().startsWith("/doc.html")){
            filterChain.doFilter(request, response);
            return;
        }
        //TODO 登录认证放入的是userId 需要修改为JWT
        String token = request.getHeader("X-TOKEN");
        if(!StringUtils.hasText(token)){
            response.getWriter().write(Res.write(50000, "token不能为空", null));
            return;
        }
        // 判断是否正确解析
        // 判断是否过期

        // 若使用JWT方式 则还需设置登录认证后的实体
        UserDetails userDetails = userDetailsService.loadUserByUsername(token);
        if(userDetails == null){
            response.getWriter().write(Res.write(50000, "当前用户不存在", null));
            return;
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()
        );
        //设置认证后的实体 以便本次请求的后续业务逻辑处理使用
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
