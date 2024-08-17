package com.zyk.base.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zyk.base.entity.AuthUser;
import com.zyk.base.mapper.AuthMapper;
import com.zyk.base.util.JwtUtil;
import com.zyk.base.util.RedisUtil;
import com.zyk.base.util.Res;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final AuthMapper authMapper;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        response.setContentType("application/json;charset=utf-8");
        // 登录接口不校验token
        if (request.getRequestURI().startsWith("/auth/login") || request.getRequestURI().startsWith("/doc.html")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 校验JWT token是否存在
        String token = request.getHeader("X-TOKEN");
        if (!StringUtils.hasText(token)) {
            response.getWriter().write(Res.write(50000, "token不能为空", null));
            return;
        }
        // 校验JWT token是否有效(secret & expired)
        if (!JwtUtil.isValidate(token)) {
            response.getWriter().write(Res.write(50000, "token无效或已失效", null));
            return;
        }

        System.out.println("X-Token: " + token);
        // 解析token
        DecodedJWT decodedToken = JwtUtil.getDecodedToken(token);
        Integer userId = decodedToken.getClaim("uid").asInt();
        String phone = decodedToken.getClaim("phone").asString();
        String name = decodedToken.getClaim("name").asString();
        String[] roleKeys = decodedToken.getClaim("roles").asArray(String.class);

        System.out.println("uid: " + userId);
        System.out.println("phone: " + phone);
        System.out.println("name: " + name);
        System.out.println("roleKeys: " + Arrays.toString(roleKeys));

        // 若使用纯JWT方式(无redis/session) 可设置登录认证后的实体 以便本次请求的后续业务逻辑处理使用
        AuthUser authUser;
        try {
            authUser = (AuthUser) userDetailsService.loadUserByUsername(phone);
        } catch (UsernameNotFoundException e) {
            response.getWriter().write(Res.write(50000, "当前用户不存在", null));
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                authUser.getUser(), authUser.getPassword(), authUser.getAuthorities()
        );
        // 设置Authentication 以使SpringSecurity校验认证通过
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
