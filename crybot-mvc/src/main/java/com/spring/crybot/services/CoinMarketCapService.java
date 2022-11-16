package com.spring.crybot.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.spring.crybot.models.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinMarketCapService {

    public String coinMarketCapRequest(String api, String body) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet request = new HttpGet("https://api.coinmarketcap.com" + api + body);
        request.addHeader("Content-Type", "application/json");

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

    public JsonObject requestToJson(String string) {
        JsonReader reader = new JsonReader(new StringReader(string));
        reader.setLenient(true);
        return JsonParser.parseReader(reader).getAsJsonObject();
    }

    public List<Article> listCoinMarketCapNews(JsonObject json) {
        JsonArray array = json.getAsJsonArray("data");
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
        return articles;
    }
}
