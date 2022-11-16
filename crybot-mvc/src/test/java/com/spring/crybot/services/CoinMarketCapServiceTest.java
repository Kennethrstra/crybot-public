package com.spring.crybot.services;

import com.google.gson.JsonObject;
import com.spring.crybot.models.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoinMarketCapServiceTest {

    CoinMarketCapService coinMarketCapService;

    @BeforeEach
    void setUp() {
        coinMarketCapService = new CoinMarketCapService();
    }

//    @Test
//    @DisplayName("CoinMarketCap Request to retrieve news")
//    void coinMarketCapRequest() {
//        String string = coinMarketCapService.coinMarketCapRequest("/content/v3/news", "?coins=2010&page=1&size=5");
//        String result = "{\"data\":[{";
//        assertTrue(string.contains(result));
//    }

//    @Test
//    void requestToJson() {
//        String string = coinMarketCapService.coinMarketCapRequest("/content/v3/news", "?coins=2010&page=1&size=5");
//        JsonObject json = coinMarketCapService.requestToJson(string);
//        String result = "{\"data\":[{";
//        assertTrue(json.toString().contains(result));
//    }

    @Test
    void listCoinMarketCapNews() {
        String string = coinMarketCapService.coinMarketCapRequest("/content/v3/news", "?coins=2010&page=1&size=5");
        JsonObject json = coinMarketCapService.requestToJson(string);
        List<Article> articles = coinMarketCapService.listCoinMarketCapNews(json);
        articles.forEach(System.out::println);
        assertEquals(4, articles.size());
    }
}