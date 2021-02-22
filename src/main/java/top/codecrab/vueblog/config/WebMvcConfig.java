package top.codecrab.vueblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.codecrab.vueblog.common.interceptor.SessionInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        InterceptorRegistration registration = registry.addInterceptor(sessionInterceptor);
        //将这个controller放行
        registration.excludePathPatterns("/error");
        //拦截全部
        registration.addPathPatterns("/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
