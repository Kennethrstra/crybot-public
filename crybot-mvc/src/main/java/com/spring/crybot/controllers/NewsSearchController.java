package com.spring.crybot.controllers;

import com.spring.crybot.models.Article;
import com.spring.crybot.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class NewsSearchController {

    private final NewsService newsService;

    @GetMapping("/news/search")
    public Collection<Article> list(String criteria) {
        return this.newsService.findNews(criteria);
    }
}
