package com.example.rate_printer.dto;

import java.util.List;

public record PairRates(String currencyPair, List<RatePoint> dates) {}

