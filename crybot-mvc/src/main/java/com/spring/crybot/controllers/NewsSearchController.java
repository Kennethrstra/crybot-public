package com.spring.crybot.controllers;

import com.spring.crybot.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NewsSearchController {

    private final NewsService newsService;
}
