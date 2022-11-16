package com.spring.crybot.services;

import com.google.gson.JsonParser;
import com.spring.crybot.models.Account;
import com.spring.crybot.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BinanceService {

    final String HMAC_SHA256 = "HmacSHA256";
    private final AccountRepository accountRepository;

    public Optional<Account> searchAccount(String name) {
        return accountRepository.findById(name);
    }

    public double getPrice(String symbol) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        String s = binanceRequest("/api/v3/avgPrice?", joinQueryParameters(parameters));
        if (s == null) {
            return 0.0;
        } else {
            log.info("BinanceService: Retrieving average price for " + symbol + " with value " + s);
            return JsonParser.parseString(s).getAsJsonObject().get("price").getAsDouble();
        }
    }

    public String getSnapshot(Optional<Account> account) {
        if (account.isEmpty()) {
            return null;
        } else {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("type", account.get().getExchange());
            parameters.put("timestamp", String.valueOf(System.currentTimeMillis()));
            return binanceRequestSigned("/sapi/v1/accountSnapshot?", joinQueryParameters(parameters), account.get().getKey1(), account.get().getKey2());
        }
    }

    private String binanceRequest(String api, String body) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet request = new HttpGet("https://api.binance.com" + api + body);

        try (CloseableHttpResponse response = httpclient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private String binanceRequestSigned(String api, String body, String secretKey, String apiKey) {
        String signature = null;
        if (secretKey != null) {
            signature = "&signature=" + getSignature(body, secretKey);
        }

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet request = new HttpGet("https://api.binance.com" + api + body + signature);
        request.addHeader("X-MBX-APIKEY", apiKey);

        try (CloseableHttpResponse response = httpclient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private String joinQueryParameters(HashMap<String, String> parameters) {
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

    private String getSignature(String data, String key) {
        byte[] hmacSha256;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return bytesToHex(hmacSha256);
    }

    private String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}