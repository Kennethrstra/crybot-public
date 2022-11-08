package com.spring.crybot.bootstrap;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataLoaderBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private DataLoader dataloader;
    private final ApplicationContext applicationContext;

    @Autowired
    public DataLoaderBootstrap(DataLoader dataloader, ApplicationContext applicationContext) {
        this.dataloader = dataloader;
        this.applicationContext = applicationContext;
    }

    public void onApplicationEvent(@NotNull ContextRefreshedEvent contextRefreshedEvent) {
        dataloader.loadData();
    }
}
