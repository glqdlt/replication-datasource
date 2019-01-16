package com.glqdlt.ex.replicationjdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Bean
    public HikariDataSource firstDataSource(){
        HikariConfig hikariConfig = getHikariConfig("");
        return new HikariDataSource(hikariConfig);
    }


    @Bean
    public HikariDataSource secondDataSource(){
        HikariConfig hikariConfig = getHikariConfig("");
        return new HikariDataSource(hikariConfig);
    }

    private HikariConfig getHikariConfig(String url) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("");
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername("");
        hikariConfig.setPassword("");
        return hikariConfig;
    }


}
