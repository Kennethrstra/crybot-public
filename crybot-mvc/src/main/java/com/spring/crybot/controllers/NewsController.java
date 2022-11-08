package com.spring.crybot.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spring.crybot.models.Article;
import com.spring.crybot.models.Keyword;
import com.spring.crybot.repositories.KeywordRepository;
import com.spring.crybot.repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/news")
public class NewsController {
    Logger logger = LogManager.getLogger(NewsController.class);

    private final NewsRepository newsRepository;
    private final KeywordRepository keywordRepository;

    @GetMapping
    Iterable<Article> getArticles() {
        return newsRepository.findAll();
    }

    @GetMapping("/{slug}")
    ResponseEntity<Object> getArticle(@PathVariable String slug) {
        return (newsRepository.existsById(slug)) ?
                new ResponseEntity<>(newsRepository.findById(slug), HttpStatus.FOUND) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    Article postArticle(@RequestBody Article article) {
        return newsRepository.save(article);
    }

    @PutMapping("/{slug}")
    ResponseEntity<Article> putArticle(@PathVariable String slug, @RequestBody Article article) {
        return (newsRepository.existsById(slug)) ?
                new ResponseEntity<>(newsRepository.save(article), HttpStatus.OK) :
                new ResponseEntity<>(newsRepository.save(article), HttpStatus.CREATED);
    }

    @DeleteMapping
    void deleteArticles() {
        newsRepository.deleteAll();
    }

    @DeleteMapping("/{slug}")
    ResponseEntity<Object> deleteArticle(@PathVariable String slug) {
        if (newsRepository.existsById(slug)) {
            newsRepository.deleteById(slug);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/coinmarketcap/{keyword}")
    public List<Article> getArticlesCoinmarketcap(@PathVariable String keyword) throws IOException {
        Optional<Keyword> k = keywordRepository.findById(keyword);
        if (k.isPresent()) {
            Connection.Response response = Jsoup.connect("https://api.coinmarketcap.com/content/v3/news?coins=" + k.get().getId() + "&page=1&size=5")
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
            logger.info("Retrieved " + articles.size() + " articles about " + keyword + " from Coinmarketcap.com ");
            return articles;
        } else {
            return null;
        }
    }

    @PostMapping("/coinmarketcap/{keyword}")
    List<Article> updateArticlesCoinmarketcap(@PathVariable String keyword) throws IOException {
        List<Article> articles = getArticlesCoinmarketcap(keyword);
        List<Article> articlesToday = new ArrayList<>();
        if (!articles.isEmpty()) {
            for (Article a : articles) {
                if (!newsRepository.existsById(a.getSlug()) &&
                        a.getDate().contains(LocalDate.now().toString()) &&
                        a.getTitle().toLowerCase().contains(keyword)) {
                    newsRepository.save(a);
                    articlesToday.add(a);
                }
            }
        }
        return articlesToday;
    }
}
