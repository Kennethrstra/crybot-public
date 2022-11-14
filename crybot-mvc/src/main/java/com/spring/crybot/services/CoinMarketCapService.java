package com.spring.crybot.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spring.crybot.models.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinMarketCapService {

    public List<Article> getArticlesCoinMarketCap(String coins) {
        try {
            Connection.Response response = Jsoup.connect("https://api.coinmarketcap.com/content/v3/news?coins=" + coins + "&page=1&size=5")
                    .timeout(20000)
                    .ignoreContentType(true)
                    .execute();
            JsonArray array = JsonParser.parseString(response.parse().body().text()).getAsJsonObject().getAsJsonArray("data");
            List<Article> articles = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                JsonObject meta = array.get(i).getAsJsonObject().getAsJsonObject("meta");
                Article article = new Article(
                        array.get(i).getAsJsonObject().get("slug").getAsString(),
                        meta.get("title").getAsString(),
                        meta.get("releasedAt").getAsString(),
                        meta.get("sourceUrl").getAsString(),
                        true);
                articles.add(article);
            }
            log.info("CoinMarketCapService: Retrieved " + articles.size() + " articles about " + coins + " from Coinmarketcap.com ");
            return articles;
        } catch (IOException e) {
            log.error("CoinMarketCapService: " + e.getMessage());
            return null;
        }
    }
}
