package com.spring.crybot.bootstrap;

import com.spring.crybot.models.Account;
import com.spring.crybot.models.Keyword;
import com.spring.crybot.models.Price;
import com.spring.crybot.repositories.AccountRepository;
import com.spring.crybot.repositories.KeywordRepository;
import com.spring.crybot.repositories.PriceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("production")
@Component
public class ProductionDataLoader extends DataLoader {
    Logger logger = LogManager.getLogger(ProductionDataLoader.class);

    private final PriceRepository priceRepository;
    private final KeywordRepository keywordRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public ProductionDataLoader(PriceRepository priceRepository, KeywordRepository keywordRepository, AccountRepository accountRepository) {
        this.priceRepository = priceRepository;
        this.keywordRepository = keywordRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void loadEnvironmentSpecificData() {
        logger.info("Loading data for production environment");
    }
}
