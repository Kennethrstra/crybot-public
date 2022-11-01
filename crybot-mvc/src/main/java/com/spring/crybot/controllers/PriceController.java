package com.spring.crybot.controllers;

import com.spring.crybot.models.Price;
import com.spring.crybot.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prices")
public class PriceController {
    private final PriceRepository priceRepository;

    @GetMapping
    Iterable<Price> getPrices() {
        return priceRepository.findAll();
    }
}

@Component
@RequiredArgsConstructor
class PriceLoader {
    private final PriceRepository priceRepository;

    @PostConstruct
    private void loadData() {
        priceRepository.saveAll(List.of(
                new Price("BTCEUR", 0.0)
        ));
    }
}