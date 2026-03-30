package com.example.rate_printer.config;

import com.example.rate_printer.logging.ClientHttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        @Value("${spring.application.name}") String applicationName, ClientHttpLoggingInterceptor loggingInterceptor
    ) {
        return builder.baseUrl(serviceUrl)
            .defaultHeader("Accept", "application/json")
            .defaultHeader("X-Client-Name", applicationName)
            .requestInterceptor(loggingInterceptor)
            .build();
    }
}
