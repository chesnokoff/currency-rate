package com.example.rate_provider.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupVersionLogger {

    private static final Logger log = LoggerFactory.getLogger(StartupVersionLogger.class);

    private final String applicationName;
    private final ObjectProvider<BuildProperties> buildPropertiesProvider;

    public StartupVersionLogger(
        @Value("${spring.application.name}") String applicationName,
        ObjectProvider<BuildProperties> buildPropertiesProvider
    ) {
        this.applicationName = applicationName;
        this.buildPropertiesProvider = buildPropertiesProvider;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logVersion() {
        BuildProperties buildProperties = buildPropertiesProvider.getIfAvailable();
        String version = buildProperties != null ? buildProperties.getVersion() : "unknown";
        String builtAt = buildProperties != null ? String.valueOf(buildProperties.getTime()) : "unknown";

        log.info(
            "Application started: name={}, version={}, builtAt={}",
            applicationName,
            version,
            builtAt
        );
    }
}
