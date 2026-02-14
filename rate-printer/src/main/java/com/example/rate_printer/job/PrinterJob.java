package com.example.rate_printer.job;

import java.time.LocalDate;
import com.example.rate_printer.dto.RatesMultiResponse;
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
            RatesMultiResponse ratesMultiResponse = client.get()
                .uri(uri -> uri.path("/rate")
                    .queryParam("pair", "USD/RUB")
                    .queryParam("pair", "USD/EUR")
                    .queryParam("since", LocalDate.now().minusDays(3))
                    .queryParam("until", LocalDate.now())
                    .build())
                .retrieve()
                .body(RatesMultiResponse.class);

            if (ratesMultiResponse == null) {
                log.warn("Server returned empty body");
                return;
            }

            log.info("Received currency rate: {}", ratesMultiResponse);
        }
        catch (Exception e) {
            log.error("Could not fetch rate", e);
        }
    }
}
