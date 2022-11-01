package com.spring.crybot.repositories;

import com.spring.crybot.models.Article;
import org.springframework.data.repository.CrudRepository;

public interface NewsRepository extends CrudRepository<Article, String> {
}
