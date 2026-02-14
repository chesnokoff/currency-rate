package com.example.rate_provider.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.example.rate_provider.dto.PairRates;
import com.example.rate_provider.dto.RatePoint;
import com.example.rate_provider.dto.RatesMultiResponse;
import org.springframework.stereotype.Service;

@Service
public class RateService {
    public RatesMultiResponse getRate(List<String> pairs, LocalDate since, LocalDate until) {
        List<PairRates> rates = pairs.stream()
            .map(p -> new PairRates(p,
                since.datesUntil(until)
                    .map(d -> new RatePoint(
                        randomBigDecimal(85, 95, 2),
                        randomBigDecimal(85, 95, 2),
                        d))
                    .toList()))
            .toList();

        return new RatesMultiResponse(rates);
    }

    private static BigDecimal randomBigDecimal(double min, double max, int scale) {
        long factor = (long)Math.pow(10, scale);

        long minVal = Math.round(min * factor);
        long maxVal = Math.round(max * factor);

        long rnd = ThreadLocalRandom.current().nextLong(minVal, maxVal + 1);

        return BigDecimal.valueOf(rnd, scale);
    }

}
