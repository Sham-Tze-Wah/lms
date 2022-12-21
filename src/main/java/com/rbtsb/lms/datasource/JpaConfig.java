package com.rbtsb.lms.datasource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
public class JpaConfig {

    @Bean
    public DataSource dataSource()
    {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/lms?allowPublicKeyRetrieval=true&useSSL=FALSE");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("1234");
        return dataSourceBuilder.build();
    }
}
