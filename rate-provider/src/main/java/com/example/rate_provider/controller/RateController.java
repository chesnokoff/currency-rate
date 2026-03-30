package com.example.rate_provider.controller;

import java.time.LocalDate;
import java.util.List;
import com.example.rate_provider.dto.RatesMultiResponse;
import com.example.rate_provider.service.RateService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RateController {

    RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping("/rate")
    public ResponseEntity<?> getRate(
        @RequestParam("pair") List<String> pairs,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until
    ) {
        RatesMultiResponse response = rateService.getRate(pairs, since, until);
        return ResponseEntity.ok(response);
    }
}
