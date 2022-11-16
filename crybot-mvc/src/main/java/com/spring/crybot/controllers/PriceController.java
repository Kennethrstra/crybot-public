package com.spring.crybot.controllers;

import com.spring.crybot.models.Price;
import com.spring.crybot.services.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prices")
public class PriceController {

    private final PriceService priceService;

    @GetMapping
    Iterable<Price> getPrices() {
        return priceService.findAllPrices();
    }
}
