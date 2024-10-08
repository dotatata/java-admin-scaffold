package com.zyk.base.config;

import com.zyk.base.interceptors.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**");
                //.excludePathPatterns("/login", "/register", "/error");
    }

    // 允许前端跨域请求 (整合SpringSecurity后 此配置失效)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 允许跨域访问的路径
                .allowedOrigins("*")              //允许跨域访问的源
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")  //允许请求方法
                .maxAge(168000)         //预检间隔时间
                .allowedHeaders("*")    //允许头部设置
                .allowCredentials(false);//是否发送证书
    }
}
