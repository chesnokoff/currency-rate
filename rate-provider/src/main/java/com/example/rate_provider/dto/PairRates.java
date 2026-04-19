package com.example.rate_provider.dto;

import java.util.List;

public record PairRates(String currencyPair, List<RatePoint> dates) {}

