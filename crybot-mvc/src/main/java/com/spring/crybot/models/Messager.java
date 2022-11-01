package com.spring.crybot.models;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Messager {

    private TelegramBot bot;
    private long chatId;

    public Messager(String str, long id) {
        this.bot = new TelegramBot(str);
        this.chatId = id;
    }

    public void sendMessage(String msg) {
        SendResponse response = bot.execute(new SendMessage(chatId, msg));
    }
}
