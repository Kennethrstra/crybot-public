package com.spring.crybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class CrybotMvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrybotMvcApplication.class, args);
    }
}