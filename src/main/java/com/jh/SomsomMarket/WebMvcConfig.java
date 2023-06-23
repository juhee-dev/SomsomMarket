package com.jh.SomsomMarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String LOGIN_FORM = "user/loginForm";

    @Autowired
    @Qualifier(value = "loginInterceptor")
    private HandlerInterceptor interceptor;

    @Value("${resources.location}")
    private String resourcesLocation;
    @Value("${resources.uri_path:}")
    private String resourcesUriPath;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user/loginForm").setViewName(LOGIN_FORM);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor) // 다음 url로 들어갈 경우, 바로 login Form으로 이동
                .addPathPatterns("/personal/register", "/group/register"); // 알아서 수정!
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcesUriPath + "/**")
                .addResourceLocations("file:///" + resourcesLocation);
    }
}
