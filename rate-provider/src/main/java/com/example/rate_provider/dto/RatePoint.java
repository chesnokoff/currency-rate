package com.example.rate_provider.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RatePoint(BigDecimal bid, BigDecimal ask, LocalDate date) {}
