package com.spring.crybot.schedulers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
@EnableScheduling
@Slf4j
public class DevelopmentScheduledTasks {

    static {
        log.info("Started DevelopmentScheduledTasks.class");
    }
}
