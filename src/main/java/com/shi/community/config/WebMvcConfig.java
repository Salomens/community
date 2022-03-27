package com.shi.community.config;

import com.shi.community.controller.interceptor.*;
import com.shi.community.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 注入拦截器
     */
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

//    @Autowired
//    private LoginRequiredInterceptor loginRequiredInterceptor;
    @Autowired
    private MessageInterceptor messageInterceptor;
    @Autowired
    private DataInterceptor dataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)//添加拦截器
                .excludePathPatterns("/**/*.css");//排除的路径

        registry.addInterceptor(loginTicketInterceptor)//添加拦截器
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")//排除的路径
                ;
//        registry.addInterceptor(loginRequiredInterceptor)//添加拦截器
//                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")//排除的路径
//        ;
        registry.addInterceptor(messageInterceptor)//添加拦截器
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");//排除的路径

        registry.addInterceptor(dataInterceptor)//添加拦截器
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");//排除的路径
    }
}
