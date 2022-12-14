package com.spring.crybot.controllers;

import com.spring.crybot.models.Account;
import com.spring.crybot.services.BinanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/binance")
@Slf4j
public class BinanceController {

    private final BinanceService binanceService;

    @GetMapping("/price/{symbol}")
    ResponseEntity<Double> price(@PathVariable String symbol) {
        double price = binanceService.getPrice(symbol);
        return (price == 0.0) ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(price, HttpStatus.FOUND);
    }

    @GetMapping("/snapshot/{name}")
    ResponseEntity<String> getSnapshot(@PathVariable String name) {
        Optional<Account> account = binanceService.searchAccount(name);
        return (account.isEmpty()) ?
                new ResponseEntity<>("Error: Account not found in database", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(binanceService.getSnapshot(account), HttpStatus.FOUND);
    }
}
