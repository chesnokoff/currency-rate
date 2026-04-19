package com.example.rate_printer.config;

import com.example.rate_printer.logging.ClientHttpLoggingInterceptor;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientsConfig {

    @Bean
    @LoadBalanced
    RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    RestClient rateProviderRestClient(
        @LoadBalanced RestClient.Builder builder,
        @Value("${rate-provider.service-url}") String serviceUrl,
        @Value("${spring.application.name}") String applicationName,
        ClientHttpLoggingInterceptor loggingInterceptor,
        @Value("${rate-provider.connect-timeout}") Duration connectTimeout,
        @Value("${rate-provider.read-timeout}") Duration readTimeout
    ) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        return builder.baseUrl(serviceUrl)
            .defaultHeader("Accept", "application/json")
            .defaultHeader("X-Client-Name", applicationName)
            .requestInterceptor(loggingInterceptor)
            .requestFactory(requestFactory)
            .build();
    }
}
