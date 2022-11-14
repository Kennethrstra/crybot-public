package com.spring.crybot.controllers;

import com.spring.crybot.models.Account;
import com.spring.crybot.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    @GetMapping
    Iterable<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/{name}")
    Optional<Account> getAccount(@PathVariable String name) {
        return accountRepository.findById(name);
    }

    @PostMapping
    Account postAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

    @PutMapping("/{name}")
    ResponseEntity<Account> putAccount(@PathVariable String name, @RequestBody Account account) {
        return (accountRepository.existsById(name)) ?
                new ResponseEntity<>(accountRepository.save(account), HttpStatus.OK) :
                new ResponseEntity<>(accountRepository.save(account), HttpStatus.CREATED);
    }

    @DeleteMapping("/{name}")
    void deleteAccount(@PathVariable String name) {
        accountRepository.deleteById(name);
    }
}
