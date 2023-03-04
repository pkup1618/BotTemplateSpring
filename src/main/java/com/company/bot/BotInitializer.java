package com.company.bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import javax.annotation.PostConstruct;

@Component
public class BotInitializer
{
    TelegramBotsApi telegramBotsApi;
    Bot bot;
    @Autowired
    public void setBot(Bot bot)
    {
        this.bot = bot;
    }

    public BotInitializer() throws TelegramApiException
    {
        this.telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
    }

    @PostConstruct
    public void initBot() throws TelegramApiException
    {
        telegramBotsApi.registerBot(bot);   // Запускается LongPollingBot, нет веб-хука
    }
}
