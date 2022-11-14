package com.spring.crybot.controllers;

import com.spring.crybot.models.Keyword;
import com.spring.crybot.repositories.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/keywords")
@Slf4j
public class KeywordController {

    private final KeywordRepository keywordRepository;

    @GetMapping
    Iterable<Keyword> getKeywords() {
        return keywordRepository.findAll();
    }

    @DeleteMapping
    void deleteKeywords() {
        keywordRepository.deleteAll();
        log.info("KeywordController: Removed all Keywords from repository");
    }

    @GetMapping("/{id}")
    ResponseEntity<Object> getKeyword(@PathVariable String id) {
        return (keywordRepository.existsById(id)) ?
                new ResponseEntity<>(keywordRepository.findById(id), HttpStatus.FOUND) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/{name}")
    ResponseEntity<Object> addKeyword(@PathVariable String id, @PathVariable String name) {
        Keyword k = new Keyword(id, name);
        return (k.getId().isEmpty()) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(keywordRepository.save(k), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/{name}")
    ResponseEntity<Object> deleteKeyword(@PathVariable String id, @PathVariable String name) {
        Keyword k = new Keyword(id, name);
        keywordRepository.delete(k);
        return (keywordRepository.findById(k.getId()).isEmpty()) ?
                new ResponseEntity<>(HttpStatus.ACCEPTED) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
