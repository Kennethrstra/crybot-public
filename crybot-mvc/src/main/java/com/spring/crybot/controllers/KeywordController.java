package com.spring.crybot.controllers;

import com.spring.crybot.models.Keyword;
import com.spring.crybot.repositories.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/keywords")
public class KeywordController {
    Logger logger = LogManager.getLogger(KeywordController.class);

    private final KeywordRepository keywordRepository;

    @GetMapping
    Iterable<Keyword> getKeywords() {
        return keywordRepository.findAll();
    }

    @DeleteMapping
    void deleteKeywords() {
        keywordRepository.deleteAll();
        logger.info("Removed all Keyword from Keyword repository");
    }

    @GetMapping("/{name}")
    ResponseEntity<Object> getKeyword(@PathVariable String name) {
        return (keywordRepository.existsById(name)) ?
                new ResponseEntity<>(keywordRepository.findById(name), HttpStatus.FOUND) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{name}/{id}")
    ResponseEntity<Object> addKeyword(@PathVariable String name, @PathVariable String id) {
        Keyword k = new Keyword(name, id);
        return (k.getKeyword().isEmpty()) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(keywordRepository.save(k), HttpStatus.CREATED);
    }

    @DeleteMapping("/{name}/{id}")
    ResponseEntity<Object> deleteKeyword(@PathVariable String name, @PathVariable String id) {
        Keyword k = new Keyword(name, id);
        keywordRepository.delete(k);
        return (keywordRepository.findById(k.getKeyword()).isEmpty()) ?
                new ResponseEntity<>(HttpStatus.ACCEPTED) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
