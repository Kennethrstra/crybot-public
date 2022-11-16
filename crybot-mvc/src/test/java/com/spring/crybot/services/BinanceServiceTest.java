package com.spring.crybot.services;

import com.spring.crybot.models.Account;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BinanceServiceTest {

    BinanceService binanceService;

    @BeforeEach
    void setUp() {
        binanceService = new BinanceService(null);
    }

    @Test
    void getPrice() {
        double price = binanceService.getPrice("BTCEUR");
        System.out.println(price);
        assertTrue(price != 0.0);
    }

    @Test
    void getSnapshot() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet request = new HttpGet("https://myexternalip.com/raw");
        String ip = "";
        try (CloseableHttpResponse response = httpclient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                ip = EntityUtils.toString(entity);
            }
        } catch (Exception ignore) {
        }
        if (ip.equals("192.168.1.1")) {
            Account account = new Account();
            String snapshot = binanceService.getSnapshot(Optional.of(account));
            System.out.println(snapshot);
            assertTrue(snapshot.contains("\"code\":200"));
        }
    }
}