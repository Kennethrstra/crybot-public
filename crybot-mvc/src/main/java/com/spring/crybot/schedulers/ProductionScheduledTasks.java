package com.spring.crybot.schedulers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spring.crybot.models.*;
import com.spring.crybot.repositories.AccountRepository;
import com.spring.crybot.repositories.KeywordRepository;
import com.spring.crybot.repositories.NewsRepository;
import com.spring.crybot.repositories.PriceRepository;
import com.spring.crybot.services.BinanceService;
import com.spring.crybot.services.CoinMarketCapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Profile("prod")
@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ProductionScheduledTasks {

    private final AccountRepository accountRepository;
    private final NewsRepository newsRepository;
    private final KeywordRepository keywordRepository;
    private final PriceRepository priceRepository;

    private final CoinMarketCapService coinMarketCapService;
    private final BinanceService binanceService;

    static {
        log.info("Started ProductionScheduledTasks.class");
    }

    @Scheduled(cron = "0 0 2 ? * *")
    void updatePrice() {
        log.info("Started Scheduled Task: updatePrice()");
        String[] symbols = {"BTCEUR"};
        for (String symbol : symbols) {
            double value = binanceService.getPrice(symbol);
            Price price = new Price(symbol, value);
            priceRepository.save(price);
            log.info("Symbol " + symbol + " updated with value of " + price.getPrice());
        }
    }

    @Scheduled(cron = "0 0 8 ? * *")
    void sendSnapshot() {
        log.info("ProductionScheduledTasks: sendSnapshot()");
        Iterable<Account> accounts = accountRepository.findAll();
        accounts.forEach((a) ->
                {
                    Optional<Account> account = Optional.of(a);
                    String s = binanceService.getSnapshot(Optional.of(a));
                    if (s.contains("\"code\":200")) {
                        double btceur = (priceRepository.findById("BTCEUR").isPresent()) ? priceRepository.findById("BTCEUR").get().getPrice() : 0.0;
                        if (btceur != 0.0) {
                            JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
                            double value = latestTotalAssetOfBtc(jsonObject);
                            BigDecimal e = BigDecimal.valueOf(btceur * value);
                            double eur = e.setScale(2, RoundingMode.HALF_UP).doubleValue();
                            new Messager("KEY:KEY", account.get().getChatId()).sendMessage("De waarde van je Binance wallet is BTC " + value + " (Euro = " + eur + ")");
                            log.info("ProductionScheduledTasks: " + account.get().getName() + "'s wallet with a value of " + eur + " send to Telegram chat ID " + account.get().getName());
                        } else {
                            log.warn("ProductionScheduledTasks: Could not get value of BTCEUR");
                        }
                    }
                }
        );
    }

    @Scheduled(cron = "0 0 0-23 ? * *")
    void updateArticles() {
        log.info("ProductionScheduledTasks: updateArticles()");
        Iterable<Keyword> keywords = keywordRepository.findAll();
        for (Keyword keyword : keywords) {
            String request = coinMarketCapService.coinMarketCapRequest("/content/v3/news", "?coins=" + keyword.getId() + "&page=1&size=5");
            JsonObject json = coinMarketCapService.requestToJson(request);
            List<Article> articles = coinMarketCapService.listCoinMarketCapNews(json);
            for (Article a : articles) {
                if (!newsRepository.existsById(a.getSlug()) && a.getDate().contains(LocalDate.now().toString()) && a.getTitle().toLowerCase().contains(keyword.getKeyword())) {
                    newsRepository.save(a);
                    log.info("ProductionScheduledTasks: Saved Article " + a.getUrl() + " to News repository");
                }
            }
        }
    }

    @Scheduled(cron = "0 ${random.int[0,59]} 8-18 ? * *")
    void dispatchArticles() {
        log.info("ProductionScheduledTasks: dispatchArticles()");
        Messager messager = new Messager("KEY:KEY", -128);
        Iterable<Article> articles = newsRepository.findAll();
        for (Article a : articles) {
            if (a.isDispatch()) {
                messager.sendMessage("Nieuw artikel - " + a.getTitle() + "\n" + a.getUrl());
                a.setDispatch(false);
                newsRepository.save(a);
                log.info("ProductionScheduledTasks: Article " + a.getUrl() + " send to Telegram chat ID " + messager.getChatId());
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
