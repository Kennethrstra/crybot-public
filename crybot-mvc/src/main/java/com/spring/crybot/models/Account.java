package com.spring.crybot.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("account")
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private String name;
    private String key1;
    private String key2;
    private String exchange;
    private long chatId;
    private boolean snapshot;
}
