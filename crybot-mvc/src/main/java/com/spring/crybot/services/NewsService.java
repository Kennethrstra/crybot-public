package com.spring.crybot.services;

import com.spring.crybot.models.Article;
import com.spring.crybot.repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public Optional<Article> searchNews(String criteria) {
        return newsRepository.findById(criteria);
    }
}
