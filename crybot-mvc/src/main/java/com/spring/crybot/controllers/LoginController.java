package com.spring.crybot.controllers;

import com.spring.crybot.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private static final String ACCOUNT_ATTRIBUTE = "account";

    private final AccountService accountService;

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpServletRequest request, HttpSession session) {
        try {
            var account = this.accountService.login(username, password);
            session.setAttribute(ACCOUNT_ATTRIBUTE, account);
            return "redirect:/index.html";
        } catch (AuthenticationException e) {
            request.setAttribute("exception", e);
            return "login";
        }
    }
}
