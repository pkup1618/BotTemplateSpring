package com.company.services;

import com.company.bot.Bot;
import com.company.data.ChatMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;


@Component
public class MessageHandler extends Thread
{
    private ChatMemberService chatMemberService;
    private Bot bot;
    private LocaleService localeService;

    @Autowired
    public void setChatMemberService(ChatMemberService chatMemberService)
    {
        this.chatMemberService = chatMemberService;
    }

    @Autowired
    public void setBot(Bot bot)
    {
        this.bot = bot;
    }

    @Autowired
    public void setLocaleService(LocaleService localeService)
    {
        this.localeService = localeService;
    }

    /**
     * Запускает цикл обработки сообщений, каждые 0.5 секунды
     * выделяется 1 поток на группу сообщений, пришедших за это время
     */
    @Override
    public void run()
    {
        while(true)
        {
            if (!bot.receivedMessages.isEmpty())
            {
                Thread thread = new Thread(() ->
                {
                    while(!bot.receivedMessages.isEmpty())
                    {
                        processMessage(bot.receivedMessages.poll());
                    }
                });
                thread.start();

                try
                {
                    sleep(500);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Основной метод обработки сообщений (корень всей обработки)
     * Распознаёт, является ли сообщение командой или зарезервированным
     * и в соответствии с этим вызывает нужный метод обработки
     * @param update - сообщение
     */
    public void processMessage(Update update)
    {
        saveUserInfo(update);

        Message message = update.getMessage();
        Long chatId = message.getChatId();
        Long userId = message.getFrom().getId();
        String locale = chatMemberService.findById(userId).getLocale();

        BotCommands command = parseCommand(update);

        switch (command)
        {
            case START -> sendMessage(chatId, locale, "startMessage");
            case HELP -> sendMessage(chatId, locale, "helpMessage");
            case LOCALE -> sendChangeLocaleMessage(chatId, locale);
            case SET_RU_LOCALE -> {
                enableLocale(userId, "ru");
                locale = "ru";
                sendMessage(chatId, locale, "locale.changeSuccess");
            }
            case SET_EN_LOCALE -> {
                enableLocale(userId, "en");
                locale = "en";
                sendMessage(chatId, locale, "locale.changeSuccess");
            }
            case UNKNOWN -> handleLikeEcho(message);
        }
    }

    /**
     * Метод установки локализации пользователю
     * @param userId id пользователя
     * @param locale локализация (ru / en)
     */
    public void enableLocale(Long userId, String locale)
    {
        chatMemberService.changeLocale(userId, locale);
    }

    /**
     * Метод отправки простого текстового сообщения
     * @param chatId чат, в который отправляем сообщение
     * @param locale на каком языке
     * @param message текст сообщения
     */
    public void sendMessage(Long chatId, String locale, String message)
    {
//        и добавить меню
        SendMessage answer = new SendMessage();
        answer.setText(localeService.getMessage(message, locale));
        answer.setChatId(chatId);

        try
        {
            bot.execute(answer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Отправляет текстовое сообщение с просьбой сменить язык
     * и 2 кнопки, с помощью которых можно выбрать язык
     * @param chatId идентификатор пользователя
     * @param locale на каком языке отправлять сообщение
     */
    public void sendChangeLocaleMessage(Long chatId, String locale)
    {
        ArrayList<KeyboardButton> buttons = new ArrayList<>();
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardButton ruLocaleButton = KeyboardButton.builder().text("RU \uD83C\uDDF7\uD83C\uDDFA").build();
        KeyboardButton enLocaleButton = KeyboardButton.builder().text("EN \uD83C\uDDFA\uD83C\uDDF8").build();

        buttons.add(ruLocaleButton);
        buttons.add(enLocaleButton);

        keyboardRows.add(new KeyboardRow(buttons));

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder().keyboard(keyboardRows).build();

        SendMessage message = new SendMessage();

        message.setReplyMarkup(replyKeyboardMarkup);
        message.setChatId(chatId);
        message.setText(localeService.getMessage("locale.chooseLanguage", locale));

        try
        {
            bot.execute(message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public BotCommands parseCommand(Update update)
    {
        Message message = update.getMessage();
        String text = message.getText();

        switch (text) {
            case "/start" -> {
                return BotCommands.START;
            }
            case "/help" -> {
                return BotCommands.HELP;
            }
            case "/locale" -> {
                return BotCommands.LOCALE;
            }
            case "RU \uD83C\uDDF7\uD83C\uDDFA" -> {
                return BotCommands.SET_RU_LOCALE;
            }
            case "EN \uD83C\uDDFA\uD83C\uDDF8" -> {
                return BotCommands.SET_EN_LOCALE;
            }
            default -> {
                return BotCommands.UNKNOWN;
            }
        }
    }

    /**
     * Сохранить информацию о пользователе
     * @param update - сообщение пользователя
     */
    public void saveUserInfo(Update update)
    {
        Message message = update.getMessage();
        Long id = message.getFrom().getId();
        ChatMember chatMember = new ChatMember();
        chatMember.setId(id);
        chatMemberService.save(chatMember);
    }

    /**
     * Отвечает на сообщение тем же текстом
     * @param message сообщение
     */
    public void handleLikeEcho(Message message)
    {
        SendMessage answer = new SendMessage();
        answer.setText(message.getText());
        answer.setChatId(message.getChatId());

        try
        {
            bot.execute(answer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}