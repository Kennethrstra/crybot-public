package com.spring.crybot.schedulers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spring.crybot.controllers.BinanceController;
import com.spring.crybot.controllers.NewsRestController;
import com.spring.crybot.models.*;
import com.spring.crybot.repositories.AccountRepository;
import com.spring.crybot.repositories.KeywordRepository;
import com.spring.crybot.repositories.NewsRepository;
import com.spring.crybot.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledTasks {
    Logger logger = LogManager.getLogger(NewsRestController.class);

    private final AccountRepository accountRepository;
    private final NewsRepository newsRepository;
    private final KeywordRepository keywordRepository;
    private final PriceRepository priceRepository;

    private final NewsRestController newsRestController;
    private final BinanceController binanceController;

    @Scheduled(cron = "0 0 2 ? * *")
    void updatePrice() {
        String[] symbols = {"BTCEUR"};
        for (String symbol : symbols) {
            double value = binanceController.getPrice(symbol);
            Price price = new Price(symbol, value);
            priceRepository.save(price);
            logger.info("Symbol " + symbol + " updated with value of " + price.getPrice());
        }
    }

    @Scheduled(cron = "0 0 8 ? * *")
    void sendSnapshotKenneth() {
        Optional<Account> account = accountRepository.findById("kenneth");
        if (account.isPresent() && account.get().isSnapshot()) {
            String s = binanceController.getSnapshot(account.get().getName());
            if (s.contains("\"code\":200")) {
                double btceur = (priceRepository.findById("BTCEUR").isPresent()) ?
                        priceRepository.findById("BTCEUR").get().getPrice() :
                        0.0;
                if (btceur != 0.0) {
                    JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
                    double value = latestTotalAssetOfBtc(jsonObject);
                    BigDecimal e = BigDecimal.valueOf(btceur * value);
                    double eur = e.setScale(2, RoundingMode.HALF_UP).doubleValue();
                    new Messager("KEY:KEY", account.get().getChatId())
                            .sendMessage("De waarde van je Binance wallet is BTC " + value + " (Euro = " + eur + ")");
                    logger.info(account.get().getName() + "'s wallet with a value of " + eur + " send to Telegram chat ID " + account.get().getChatId());
                } else {
                    logger.warn("Could not get value of BTCEUR");
                }
            }
        }
    }

    @Scheduled(cron = "0 0 0-23 ? * *")
    void updateArticles() throws IOException {
        Iterable<Keyword> keywords = keywordRepository.findAll();
        for (Keyword keyword : keywords) {
            List<Article> coinmarketcapArticles = newsRestController.getArticlesCoinmarketcap(keyword.getKeyword());
            for (Article a : coinmarketcapArticles) {
                if (!newsRepository.existsById(a.getSlug()) &&
                        a.getDate().contains(LocalDate.now().toString()) &&
                        a.getTitle().toLowerCase().contains(keyword.getKeyword())) {
                    newsRepository.save(a);
                    logger.info("Saved Article " + a.getUrl() + " to News repository");
                }
            }
        }
    }

    @Scheduled(cron = "0 ${random.int[0,59]} 8-18 ? * *")
    void dispatchArticles() {
        Messager messager = new Messager("KEY:KEY", -123456789);
        Iterable<Article> articles = newsRepository.findAll();
        for (Article a : articles) {
            if (a.isDispatch()) {
                messager.sendMessage(
                        "Nieuw artikel - " +
                                a.getTitle() +
                                "\n" +
                                a.getUrl());
                a.setDispatch(false);
                newsRepository.save(a);
                logger.info("Article " + a.getUrl() + " send to Telegram chat ID " + messager.getChatId());
            }
        }
    }

    double latestTotalAssetOfBtc(JsonObject jsonObject) {
        JsonArray child = jsonObject.getAsJsonObject().getAsJsonArray("snapshotVos");
        ArrayList<String> btc = new ArrayList<>();
        for (JsonElement result : child) {
            JsonObject data = result.getAsJsonObject().getAsJsonObject("data");
            btc.add(data.get("totalAssetOfBtc").getAsString());
        }
        return Double.parseDouble(btc.get(btc.size() - 1));
    }
}
