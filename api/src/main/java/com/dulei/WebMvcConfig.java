package com.dulei;

import com.dulei.controller.interceptor.MiniInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public MiniInterceptor miniIntercepror(){
        return new MiniInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(miniIntercepror())
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/queryIsLike")
                .addPathPatterns("/bgm/**")
                .addPathPatterns("/video/upload","/video/likeVideo","/video/unLikeVideo");
        super.addInterceptors(registry);
    }
}
