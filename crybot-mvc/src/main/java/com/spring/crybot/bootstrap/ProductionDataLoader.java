package com.spring.crybot.bootstrap;

import com.spring.crybot.models.Account;
import com.spring.crybot.models.Keyword;
import com.spring.crybot.models.Price;
import com.spring.crybot.repositories.AccountRepository;
import com.spring.crybot.repositories.KeywordRepository;
import com.spring.crybot.repositories.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("prod")
@Component
@Slf4j
public class ProductionDataLoader extends DataLoader {

    private final PriceRepository priceRepository;
    private final KeywordRepository keywordRepository;
    private final AccountRepository accountRepository;

    static {
        log.info("Started ProductionDataLoader.class");
    }

    @Autowired
    public ProductionDataLoader(PriceRepository priceRepository, KeywordRepository keywordRepository, AccountRepository accountRepository) {
        this.priceRepository = priceRepository;
        this.keywordRepository = keywordRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void loadEnvironmentSpecificData() {
        log.info("ProductionDataLoader: Loading data for production environment");
    }
}
