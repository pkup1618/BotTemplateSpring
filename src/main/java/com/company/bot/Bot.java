package com.company.bot;


import com.company.services.ChatMemberService;
import com.company.services.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Properties;

@Component
public class Bot extends org.telegram.telegrambots.bots.TelegramLongPollingBot
{
    ChatMemberService chatMemberService;
    @Autowired
    public void setChatMemberService(ChatMemberService chatMemberService)
    {
        this.chatMemberService = chatMemberService;
    }

    PropertiesService propertiesService;
    @Autowired
    public void setPropertiesService(PropertiesService propertiesService)
    {
        this.propertiesService = propertiesService;
    }

    private boolean convertingToUppercase = false;

    Bot(DefaultBotOptions botOptions, String botToken)
    {
        super(botOptions, botToken);
    }

    /**
     * Метод для приема сообщений.
     * @param update Содержит сообщение от пользователя.
     */
    @Override
    public void onUpdateReceived(Update update)
    {
        Message message = update.getMessage();
        saveUserInfo(update);

        if (message.isCommand())
        {
            handleCommand(update);
        }

        handleLikeEcho(update);
    }

    public void saveUserInfo(Update update)
    {
        Message message = update.getMessage();
        Long id = message.getFrom().getId();

        chatMemberService.save(id);
    }

    /**
     * Отвечает на сообщение тем же текстом
     * @param update сообщение
     */
    public void handleLikeEcho(Update update)
    {
        Message message = update.getMessage();
        sendMsg(message.getChatId().toString(), message.getText());
    }

    /**
     * Обработчик команд <br> <br> Вызывается, если получено сообщение с командой
     * @param update сообщение
     */
    public void handleCommand(Update update)
    {
        Message message = update.getMessage();
        String messageText = message.getText();

        switch (messageText)
        {
            case "/help":
            {
                // send help message :)
            }
            case "/scream":
            {
                convertingToUppercase = !convertingToUppercase;
            }
            case "/turnoff":
            {
                String answer = "Я хотел, чтобы бот выключался, но не знаю, как это сделать";
                sendMsg(message.getChatId().toString(), answer);
            }
            // другие команды
        }
    }

    /**
     * Метод для настройки сообщения и его отправки.
     * @param chatId id чата
     * @param text Строка, которую необходимо отправить в качестве сообщения.
     */
    public synchronized void sendMsg(String chatId, String text)
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try
        {
            execute(sendMessage);
        }
        catch (TelegramApiException e)
        {
            //
        }
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