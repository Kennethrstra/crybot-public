package com.spring.crybot.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("article")
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    private String slug;
    private String title;
    private String date;
    private String url;
    private boolean dispatch;
}
