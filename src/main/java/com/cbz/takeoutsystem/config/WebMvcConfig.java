package com.cbz.takeoutsystem.config;

import com.cbz.takeoutsystem.interceptor.ClientLoginInterceptor;
import com.cbz.takeoutsystem.interceptor.ServerLoginInterceptor;
import com.cbz.takeoutsystem.interceptor.SystemInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper jsonMapper;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SystemInterceptor(redisTemplate, jsonMapper)).excludePathPatterns(
                //排除路径
                "/backend/**",
                "/front/**",
                "/common/**",
                "/employee/login",
                "/user/login",
                "/user/code"
        ).order(0);

        registry.addInterceptor(new ServerLoginInterceptor()).addPathPatterns(
                //server端特有路径
                "/employee/**"
        ).excludePathPatterns(
                "/employee/login"
        ).order(1);

        registry.addInterceptor(new ClientLoginInterceptor()).addPathPatterns(
                //客户端特有路径 /user/**
                "/user/**"
        ).excludePathPatterns(
                "/user/login",
                "/user/code"
        ).order(2);
    }
}
