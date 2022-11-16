package com.spring.crybot.services;

import com.spring.crybot.models.Price;
import com.spring.crybot.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;

    public Iterable<Price> findAllPrices() {
        return priceRepository.findAll();
    }
}
