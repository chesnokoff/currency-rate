package com.example.rate_printer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.example.rate_printer.dto.RatesMultiResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.client.RestClient;

@ExtendWith(PactConsumerTestExt.class)
class RateProviderPactConsumerTest {
    @Pact(consumer = "rate-printer", provider = "rate-provider")
    V4Pact rateContract(PactDslWithProvider builder) {
        DslPart body = new PactDslJsonBody()
            .minArrayLike("pairs", 1)
                .stringMatcher("currencyPair", "^[A-Z]{3}/[A-Z]{3}$", "USD/RUB")
                .minArrayLike("dates", 1)
                    .decimalType("bid", 90.12)
                    .decimalType("ask", 91.12)
                    .stringMatcher("date", "^\\d{4}-\\d{2}-\\d{2}$", "2026-03-01")
                .closeObject()
                .closeArray()
            .closeObject()
            .closeArray();

        return builder
            .uponReceiving("a request for currency rates in period")
                .path("/rate")
                .query("pair=USD/RUB&pair=USD/EUR&since=2026-03-01&until=2026-03-04")
                .method("GET")
            .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(body)
            .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "rateContract")
    void shouldConsumeRatesFromProvider(MockServer mockServer) {
        RestClient client = RestClient.builder()
            .baseUrl(mockServer.getUrl())
            .defaultHeader("Accept", "application/json")
            .build();

        RatesMultiResponse response = client.get()
            .uri("/rate?pair=USD/RUB&pair=USD/EUR&since=2026-03-01&until=2026-03-04")
            .retrieve()
            .body(RatesMultiResponse.class);

        assertEquals("USD/RUB", response.pairs().get(0).currencyPair());
    }
}
