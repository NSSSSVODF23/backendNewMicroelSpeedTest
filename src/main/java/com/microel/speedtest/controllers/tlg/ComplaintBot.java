package com.microel.speedtest.controllers.tlg;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

@Component
public class ComplaintBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return "microel_tech_bot";
    }

    @Override
    public String getBotToken() {
        return "5492162904:AAHSbHw8WE6F8tFnd4vSc_CU2r00M0nn5XU";
    }
}
