package com.example.rate_provider.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import com.example.rate_provider.dto.RateDTO;
import org.springframework.stereotype.Service;

@Service
public class RateService {
    public RateDTO getRate(String base, String target) {
        return new RateDTO(
            base,
            target,
            randomBigDecimal(85, 95, 2),
            randomBigDecimal(85, 95, 2),
            Instant.now()
        );
    }

    private static BigDecimal randomBigDecimal(double min, double max, int scale) {
        long factor = (long)Math.pow(10, scale);

        long minVal = Math.round(min * factor);
        long maxVal = Math.round(max * factor);

        long rnd = ThreadLocalRandom.current().nextLong(minVal, maxVal + 1);

        return BigDecimal.valueOf(rnd, scale);
    }

}
