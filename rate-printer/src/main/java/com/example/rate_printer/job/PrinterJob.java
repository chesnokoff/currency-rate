package com.example.rate_printer.job;

import com.example.rate_printer.dto.RateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PrinterJob {
    private static final Logger log = LoggerFactory.getLogger(PrinterJob.class);

    private final RestClient client;

    public PrinterJob(RestClient client) {
        this.client = client;
    }

    @Scheduled(fixedDelayString = "${printer.period-ms:5000}")
    public void printRate() {
        try {
            RateDTO rateDTO = client.get()
                .uri(uri -> uri.path("/rate")
                    .queryParam("base", "USD")
                    .queryParam("target", "RUB")
                    .build())
                .retrieve()
                .body(RateDTO.class);

            if (rateDTO == null) {
                log.warn("Server returned empty body");
                return;
            }

            log.info("Received currency rate: {}", rateDTO);
        }
        catch (Exception e) {
            log.error("Could not fetch rate", e);
        }
    }
}
