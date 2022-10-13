package com.microel.speedtest.controllers.tlg;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Service
public class TelegramController extends TelegramBotsApi {
    public TelegramController(ComplaintBot complaintBot) throws TelegramApiException {
        super(DefaultBotSession.class);
        this.registerBot(complaintBot);
    }
}
