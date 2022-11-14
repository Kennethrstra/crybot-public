package com.spring.crybot.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("keyword")
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {

    @Id
    String id;
    String keyword;
}
