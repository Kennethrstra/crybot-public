package com.spring.crybot.repositories;

import com.spring.crybot.models.Keyword;
import org.springframework.data.repository.CrudRepository;

public interface KeywordRepository extends CrudRepository<Keyword, String> {
}
