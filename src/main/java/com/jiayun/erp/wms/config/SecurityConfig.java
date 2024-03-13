package com.jiayun.erp.wms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiayun.erp.wms.filter.AuthenticationTokenFilter;
import com.jiayun.erp.wms.mapper.UserMapper;
import com.jiayun.erp.wms.util.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 使用注解进行访问权限鉴权 prePostEnabled=true 支持使用@PreAuthorize
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationTokenFilter authenticationTokenFilter;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用SpringSecurity的默认登录认证
        //super.configure(http);

        http.cors().and()           // 开启跨域
                .csrf().disable();  // 禁用CSRF保护
        // 根据请求的URL进行权限控制
        http.authorizeRequests()
                //.antMatchers("/login.html").permitAll()             // 允许所有用户访问登录页面 前后端分离不需要配置该项
                //.antMatchers("/login").permitAll()                  // SpringSecurity的默认登录接口
                .antMatchers("/auth/login").permitAll()   // 自定义逻辑的登录接口 不能与/login冲突
                //.antMatchers("/doc.html").permitAll()     // swagger接口文档路径
                // 权限方式鉴权 配置访问路径所需对应权限
                //.antMatchers(HttpMethod.POST, "/user").hasAuthority("user:create")
                //.antMatchers(HttpMethod.DELETE, "/user/**").hasAuthority("user:delete")
                //.antMatchers(HttpMethod.PUT, "/user/**").hasAuthority("user:update")
                //.antMatchers(HttpMethod.GET, "/users").hasAnyAuthority("user:query", "user:get-all", "user:get-with-roles")
                // 角色方式鉴权 配置访问路径所需对应角色
                //.antMatchers(HttpMethod.POST, "/user").hasRole("admin")
                //.antMatchers(HttpMethod.DELETE, "/user/**").hasRole("admin")
                //.antMatchers(HttpMethod.PUT, "/user/**").hasRole("admin")
                //.antMatchers(HttpMethod.GET, "/users").hasAnyRole("admin", "test")
                .anyRequest().authenticated();                  // 其他请求需要认证

        http.formLogin();
                //.loginPage("/login.html")           // 登录页面URL 前后端分离不需要配置该项
                //.loginProcessingUrl("/login")       // 登录请求处理URL
                //.usernameParameter("username")      // 登录请求中的账号参数名称
                //.passwordParameter("password")      // 登录请求中的密码参数名称
                //.defaultSuccessUrl("/dashboard")    // 登录成功后的默认跳转页面 前后端分离不需要配置该项
                //.failureForwardUrl("/login")        // 登录失败后的默认跳转页面 前后端分离不需要配置该项
                // 登录成功处理逻辑 使用匿名内部类 和defaultSuccessUrl同时配置时 后配置的项覆盖前配置的项
                //.successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                //    httpServletResponse.setContentType("application/json;charset=utf-8");
                //    httpServletResponse.getWriter().write(Res.write(20000, "登录成功了~~", null));
                //})
                // 登录失败处理逻辑 使用匿名内部类 和defaultSuccessUrl同时配置时 后配置的项覆盖前配置的项
                //.failureHandler((request, response, exception) -> {
                //    response.setContentType("application/json;charset=utf-8");
                //    response.getWriter().write(Res.write(50000, "登录失败了!!", exception.getMessage()));
                //});

        http.logout()
                //.logoutUrl("/auth/logout")
                //.logoutSuccessUrl("/login")   // 登出成功后的默认跳转页面 前后端分离不需要配置该项
                .logoutSuccessHandler((request, response, authentication) -> {
                    //TODO 删除Redis缓存等处理
                });

        // 权限异常时的处理配置
        http.exceptionHandling()
                //.accessDeniedPage("/no-permission.html")
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    //Map<String, String> data = new HashMap<>();
                    //data.put("code", "200");
                    //data.put("msg", "没有操作权限!!");
                    //data.put("data", accessDeniedException.getMessage());

                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(Res.write(50000, "没有操作权限!!", accessDeniedException.getMessage()));
                });

        // session管理配置
        http.sessionManagement()
                // 关闭session创建 默认的单机session无法支持分布式部署 自定义使用Redis或JWT统一管理逻辑
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // SpringSecurity获取用户信息
    //@Bean
    //@Override
    //public UserDetailsService userDetailsService(){
    //    // 基于内存方式的用户信息
    //    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    //    manager.createUser(
    //            User.withUsername("13888888888")
    //                .password("ooxxooxx")
    //                .roles("admin", "test")
    //                .authorities("user:create", "user:delete", "user:update", "user:get-list")
    //                .build());
    //
    //    return manager;
    //}

    // 密码加密器
    @Bean
    public PasswordEncoder passwordEncoder(){
        // 明文加密器，不能用于生产
        //return NoOpPasswordEncoder.getInstance();
        // BlowFish加密算法 可以通过调整工作因子(work factor)来平衡加密速度和安全性 默认工作因子为10 意味着进行2^10次哈希运算
        return new BCryptPasswordEncoder();
        // 基于密码、盐（salt）和迭代次数
        //return new Pbkdf2PasswordEncoder();
    }

    // 密码认证的加密规则
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
