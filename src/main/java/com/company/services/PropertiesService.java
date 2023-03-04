package com.company.services;



import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@Service
public class PropertiesService
{
    public Properties loadProperties(String propsDir)
    {
        Properties properties = new Properties();
        try
        {
            properties.load(new FileInputStream(propsDir));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            //todo как-то уведомить пользователя о необходимости создать файл свойств
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return properties;
    }

    public Properties loadDbConnectionProperties()
    {
        return loadProperties("db_connection_props");
    }

    public Properties loadTgBotProperties()
    {
        return loadProperties("tg_bot_props");
    }
}
