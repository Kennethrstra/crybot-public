package com.spring.crybot.controllers;

import com.google.gson.JsonParser;
import com.spring.crybot.models.Account;
import com.spring.crybot.repositories.AccountRepository;
import com.spring.crybot.services.BinanceService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/binance")
public class BinanceController {
    Logger logger = LogManager.getLogger(BinanceController.class);

    private final AccountRepository accountRepository;

    @GetMapping("/price/{symbol}")
    public double getPrice(@PathVariable String symbol) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        String s = new BinanceService().binanceRequest("/api/v3/avgPrice?", joinQueryParameters(parameters));
        logger.info("Retrieving average price for " + symbol + " with value " + s);
        return JsonParser.parseString(s).getAsJsonObject().get("price").getAsDouble();
    }

    @GetMapping("/snapshot/{name}")
    public String getSnapshot(@PathVariable String name) {
        Account account = accountRepository.findById(name).orElse(null);
        if (account == null) {
            logger.warn("Account " + name + " does not exist in the DB");
            return new HttpEntity<>(HttpStatus.NOT_FOUND).toString();
        } else {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("type", account.getExchange());
            parameters.put("timestamp", String.valueOf(System.currentTimeMillis()));
            logger.info("Retrieving Binance snapshot for account " + name);
            return new BinanceService().binanceRequestSigned("/sapi/v1/accountSnapshot?", joinQueryParameters(parameters), account.getKey1(), account.getKey2());
        }
    }

    static String joinQueryParameters(HashMap<String, String> parameters) {
        String urlPath = "";
        boolean isFirst = true;
        for (Map.Entry mapElement : parameters.entrySet()) {
            if (isFirst) {
                isFirst = false;
                urlPath += mapElement.getKey() + "=" + mapElement.getValue();
            } else {
                urlPath += "&" + mapElement.getKey() + "=" + mapElement.getValue();
            }
        }
        return urlPath;
    }
}