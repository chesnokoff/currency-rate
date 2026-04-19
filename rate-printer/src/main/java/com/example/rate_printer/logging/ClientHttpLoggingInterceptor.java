package com.example.rate_printer.logging;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ClientHttpLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ClientHttpLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(
        HttpRequest request,
        byte[] body,
        ClientHttpRequestExecution execution
    ) throws IOException {
        long startedAt = System.nanoTime();

        log.info(
            "Outgoing request: method={}, uri={}, headers={}",
            request.getMethod(),
            request.getURI(),
            request.getHeaders()
        );

        ClientHttpResponse response = execution.execute(request, body);
        long durationMs = (System.nanoTime() - startedAt) / 1_000_000;

        log.info(
            "Incoming response: method={}, uri={}, status={}, durationMs={}",
            request.getMethod(),
            request.getURI(),
            response.getStatusCode(),
            durationMs
        );

        return response;
    }
}
