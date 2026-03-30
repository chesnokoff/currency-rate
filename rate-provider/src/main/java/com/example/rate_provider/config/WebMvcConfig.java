package com.example.rate_provider.config;

import com.example.rate_provider.interceptor.ServerHttpLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ServerHttpLoggingInterceptor serverHttpLoggingInterceptor;

    public WebMvcConfig(ServerHttpLoggingInterceptor serverHttpLoggingInterceptor) {
        this.serverHttpLoggingInterceptor = serverHttpLoggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serverHttpLoggingInterceptor).excludePathPatterns("/actuator/**");
    }
}
