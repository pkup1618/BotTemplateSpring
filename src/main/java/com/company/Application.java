package com.company;


import com.company.services.MessageHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(entityManagerFactoryRef = "modelEntityManagerFactory",
                       transactionManagerRef = "modelTransactionManager")
public class Application
{

    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        MessageHandler messageHandler = ctx.getBean("messageHandler", MessageHandler.class);
        messageHandler.start();
    }
}