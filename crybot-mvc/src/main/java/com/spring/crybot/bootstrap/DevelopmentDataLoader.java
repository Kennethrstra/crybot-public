package com.spring.crybot.bootstrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("development")
@Component
public class DevelopmentDataLoader extends DataLoader {
    Logger logger = LogManager.getLogger(ProductionDataLoader.class);

    @Autowired
    public DevelopmentDataLoader() {
    }

    @Override
    public void loadEnvironmentSpecificData() {
        logger.info("Loading data for development environment");
    }
}
