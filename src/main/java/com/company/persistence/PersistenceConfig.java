package com.company.persistence;


import com.company.services.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

@Configuration
public class PersistenceConfig
{
    PropertiesService propertiesService;

    @Autowired
    public void setPropertiesService(PropertiesService propertiesService)
    {
        this.propertiesService = propertiesService;
    }

    @Bean
    PlatformTransactionManager modelTransactionManager()
    {
        return new JpaTransactionManager(Objects.requireNonNull(modelEntityManagerFactory().getObject()));
    }

    @Bean
    LocalContainerEntityManagerFactoryBean modelEntityManagerFactory()
    {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setDataSource(modelDataSource());
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setPackagesToScan("com.company.data");

        Properties prop = new Properties();
        prop.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        prop.setProperty("hibernate.ddl-auto", "update"); //create on first start
        factoryBean.setJpaProperties(prop);

        return factoryBean;

        //todo сделать проверку, существуют ли уже таблицы

    }

    @Bean
    @Primary //todo почему primary?
    DataSource modelDataSource()
    {
        Properties dbConnectionInfo = propertiesService.loadDbConnectionProperties();

        return DataSourceBuilder
                .create()
                .username(dbConnectionInfo.getProperty("username"))
                .password(dbConnectionInfo.getProperty("password"))
                .url(dbConnectionInfo.getProperty("db_url"))
                .driverClassName("org.postgresql.Driver")
                .build();
        //todo это работает неправильно. Мне нужно загружать данные во время работы, а не компиляции
    }
}