package com.company.bot;


import com.company.services.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class Bot extends TelegramLongPollingBot
{
    PropertiesService propertiesService;

    @Autowired
    public void setPropertiesService(PropertiesService propertiesService)
    {
        this.propertiesService = propertiesService;
    }

    public final Queue<Update> receivedMessages = new ConcurrentLinkedQueue<>();

    Bot(DefaultBotOptions botOptions, String botToken)
    {
        super(botOptions, botToken);
    }

    /**
     * Метод для приема сообщений.
     * Все сообщения отправляются в хранилище.
     * @param update Содержит сообщение от пользователя.
     */
    @Override
    public void onUpdateReceived(Update update)
    {
        receivedMessages.add(update);
    }

    /**
     * Метод возвращает имя бота, указанное при регистрации.
     * @return имя бота
     */
    @Override
    public String getBotUsername()
    {
        return "Презентационный бот";
        //todo вынести в файл свойств
    }
}