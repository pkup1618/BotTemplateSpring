package com.company.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Предоставляет все используемые в ресурсах строковые данные на нужном языке
 * Для их добавления в папку resources нужно добавить соответствующий файл локализации
 */
@Component
public class LocaleService
{
    MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    /**
     * Получить локализованное сообщение
     * @param message свойство из .properties
     * @param locale локализация - ru, en и другие
     * @return локализованное сообщение
     */
    public String getMessage(String message, String locale) {
        return messageSource.getMessage(message, null, Locale.forLanguageTag(locale));
    }
}
