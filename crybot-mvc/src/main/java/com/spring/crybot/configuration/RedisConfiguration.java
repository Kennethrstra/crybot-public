package com.spring.crybot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

    @Profile("prod")
    @Bean
    public LettuceConnectionFactory redisFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("192.168.1.201", 6379));
    }

    @Profile("prod")
    @Bean
    public RedisTemplate<?, ?> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}