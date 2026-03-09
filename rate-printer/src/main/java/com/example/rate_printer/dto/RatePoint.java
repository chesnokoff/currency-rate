package com.example.rate_printer.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RatePoint(BigDecimal bid, BigDecimal ask, LocalDate date) {}
