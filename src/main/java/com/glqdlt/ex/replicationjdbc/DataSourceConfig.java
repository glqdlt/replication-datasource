package com.glqdlt.ex.replicationjdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@EnableTransactionManagement
@Configuration
public class DataSourceConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(lazyConnectionDataSourceProxy());
        em.setPackagesToScan("com.glqdlt.ex.**");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "none");
        properties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

        return properties;
    }


    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public DataSource routingDatasource(){
        Map<Object, Object> sources = new HashMap<>();
        sources.put(DataSourceType.MASTER, firstDataSource());
        sources.put(DataSourceType.SLAVE_1, secondDataSource());
        sources.put(DataSourceType.SLAVE_2, thirdDataSource());
        RoutingDatasource routingDatasource = new RoutingDatasource();
        routingDatasource.setTargetDataSources(sources);
        return routingDatasource;
    }

    @Bean
    public LazyConnectionDataSourceProxy lazyConnectionDataSourceProxy() {
        return new LazyConnectionDataSourceProxy(routingDatasource());
    }

    @Bean
    public DataSource firstDataSource() {
        HikariConfig hikariConfig = hikariConfigGenerate("jdbc:mysql://127.0.0.1:3306/some");
        return new HikariDataSource(hikariConfig);
    }


    @Bean
    public DataSource secondDataSource() {
        HikariConfig hikariConfig = hikariConfigGenerate("jdbc:mysql://127.0.0.1:3306/some2");
        return new HikariDataSource(hikariConfig);
    }


    @Bean
    public DataSource thirdDataSource() {
        HikariConfig hikariConfig = hikariConfigGenerate("jdbc:mysql://127.0.0.1:3306/some3");
        return new HikariDataSource(hikariConfig);
    }

    private HikariConfig hikariConfigGenerate(String url) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setUsername("test");
        hikariConfig.setPassword("12345");
        return hikariConfig;
    }

}
