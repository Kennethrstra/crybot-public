package com.spring.crybot.services;

import com.spring.crybot.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public String login(String username, String password) throws AuthenticationException {
        return null;
    }
}
