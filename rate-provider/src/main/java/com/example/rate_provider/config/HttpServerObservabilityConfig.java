package com.example.rate_provider.config;

import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.DefaultServerRequestObservationConvention;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.http.server.observation.ServerRequestObservationConvention;
import org.springframework.util.StringUtils;

@Configuration
public class HttpServerObservabilityConfig {

    private static final String CLIENT_HEADER = "X-Client-Name";
    private static final String UNKNOWN_CLIENT = "unknown";

    @Bean
    ServerRequestObservationConvention serverRequestObservationConvention() {
        return new DefaultServerRequestObservationConvention() {
            @Override
            public KeyValues getLowCardinalityKeyValues(ServerRequestObservationContext context) {
                String clientName = UNKNOWN_CLIENT;
                if (context.getCarrier() != null) {
                    String headerValue = context.getCarrier().getHeader(CLIENT_HEADER);
                    if (StringUtils.hasText(headerValue)) {
                        clientName = headerValue;
                    }
                }

                return super.getLowCardinalityKeyValues(context).and(KeyValue.of("client_name", clientName));
            }
        };
    }
}
