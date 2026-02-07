package com.example.rate_provider.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record RateDTO (
    String base,
    String target,
    BigDecimal bid,
    BigDecimal ask,
    Instant timestamp
) {}
