package com.example.rate_provider.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ServerHttpLoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ServerHttpLoggingInterceptor.class);
    private static final String STARTED_AT_ATTRIBUTE = ServerHttpLoggingInterceptor.class.getName() + ".startedAt";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(STARTED_AT_ATTRIBUTE, System.nanoTime());
        log.info(
            "Incoming request: method={}, uri={}, query={}, client={}",
            request.getMethod(),
            request.getRequestURI(),
            request.getQueryString(),
            request.getHeader("X-Client-Name")
        );
        return true;
    }

    @Override
    public void afterCompletion(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        Exception ex
    ) {
        long startedAt = (Long)request.getAttribute(STARTED_AT_ATTRIBUTE);
        long durationMs = (System.nanoTime() - startedAt) / 1_000_000;

        log.info(
            "Outgoing response: method={}, uri={}, status={}, durationMs={}",
            request.getMethod(),
            request.getRequestURI(),
            response.getStatus(),
            durationMs
        );
    }
}
