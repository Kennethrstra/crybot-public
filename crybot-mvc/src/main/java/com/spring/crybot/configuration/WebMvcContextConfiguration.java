package com.spring.crybot.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcContextConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/news.html").setViewName("news");
        registry.addViewController("/about.html").setViewName("about");
        registry.addViewController("/contact.html").setViewName("contact");
    }
}
