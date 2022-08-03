package com.jiang.learn.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 定义一个配装器，用来注册拦截器
 */
@Configuration
public class ControllerAdapter implements WebMvcConfigurer {

    @Autowired
    private UserLoginInterceptor userLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor)
                //进行拦截
                .addPathPatterns("/*")
                //不进行拦截
                .excludePathPatterns("/api/*");
    }
}
