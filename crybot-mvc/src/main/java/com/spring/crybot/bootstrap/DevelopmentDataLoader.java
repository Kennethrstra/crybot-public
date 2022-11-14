package com.spring.crybot.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
@Slf4j
public class DevelopmentDataLoader extends DataLoader {

    static {
        log.info("Started DevelopmentDataLoader.class");
    }

    @Autowired
    public DevelopmentDataLoader() {
    }

    @Override
    public void loadEnvironmentSpecificData() {
    }
}
