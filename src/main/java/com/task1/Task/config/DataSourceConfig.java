package com.task1.Task.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;

@Configuration
@Log4j2
public class DataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        log.debug("Creating primary data source");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "transactionManager")
    @Primary
    public JpaTransactionManager dbTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(primaryDataSource());
        return transactionManager;
    }

    @Bean("flowableDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.flowable")
    public DataSource flowableDataSource() {
        log.debug("Creating flowable data source");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "flowableTransactionManager")
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(flowableDataSource());
        return transactionManager;
    }
}
