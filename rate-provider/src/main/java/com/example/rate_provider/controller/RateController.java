package com.example.rate_provider.controller;

import com.example.rate_provider.dto.RateDTO;
import com.example.rate_provider.service.RateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RateController {

    RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping("rate")
    public ResponseEntity<?> getRate(String base, String target) {
        RateDTO rateDTO = rateService.getRate(base, target);
        return new ResponseEntity<>(rateDTO, HttpStatus.OK);
    }
}
