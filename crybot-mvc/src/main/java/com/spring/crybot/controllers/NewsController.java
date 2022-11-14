package com.spring.crybot.controllers;

import com.spring.crybot.models.Article;
import com.spring.crybot.repositories.NewsRepository;
import com.spring.crybot.services.CoinMarketCapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/news")
@Slf4j
public class NewsController {

    private final NewsRepository newsRepository;

    private final CoinMarketCapService coinMarketCapService;

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

    @GetMapping("/coinmarketcap/{coins}")
    ResponseEntity<List<Article>> getArticlesCoinMarketCap(@PathVariable String coins) {
        List<Article> articles = coinMarketCapService.getArticlesCoinMarketCap(coins);
        return (articles.isEmpty()) ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(articles, HttpStatus.FOUND);
    }
}
