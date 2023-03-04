package com.company.bot;

import com.company.services.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
public class BotConfig
{
    PropertiesService propertiesService;
    @Autowired
    public void setPropertiesService(PropertiesService propertiesService)
    {
        this.propertiesService = propertiesService;
    }

    @Bean
    public DefaultBotOptions botOptions() {
        return new DefaultBotOptions();
    }

    @Bean
    public String botToken() {
        return propertiesService.loadTgBotProperties().getProperty("token");
    }

}
