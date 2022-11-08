package com.spring.crybot.services;

import com.spring.crybot.models.Article;
import com.spring.crybot.repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public Collection<Article> findNews(String criteria) {
        return null;
    }
}
