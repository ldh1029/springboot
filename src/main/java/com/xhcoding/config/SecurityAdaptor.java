package com.xhcoding.config;

import com.xhcoding.interceptor.MyIntercetor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by xin on 2017/11/28.
 */
@Configuration
public class SecurityAdaptor extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyIntercetor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
