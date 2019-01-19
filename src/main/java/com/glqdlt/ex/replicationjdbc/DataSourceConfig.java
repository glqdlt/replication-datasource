package com.glqdlt.ex.replicationjdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.shardingsphere.api.algorithm.masterslave.MasterSlaveLoadBalanceAlgorithmType;
import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@EnableTransactionManagement
@Configuration
public class DataSourceConfig {

    @Bean(name = "myDataSource")
    public DataSource dataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds_master", firstDataSource());
        dataSourceMap.put("ds_slave0", secondDataSource());
        dataSourceMap.put("ds_slave1", thirdDataSource());
        MasterSlaveRuleConfiguration masterSlaveRuleConfiguration = new MasterSlaveRuleConfiguration();
        masterSlaveRuleConfiguration.setName("ds_master_slave");
        masterSlaveRuleConfiguration.setLoadBalanceAlgorithm(MasterSlaveLoadBalanceAlgorithmType.RANDOM.getAlgorithm());
        masterSlaveRuleConfiguration.setMasterDataSourceName("ds_master");
        masterSlaveRuleConfiguration.setSlaveDataSourceNames(Arrays.asList("ds_slave0", "ds_slave1"));
        Properties props = new Properties();
        props.put("sql.show",false);
        return MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, masterSlaveRuleConfiguration, new HashMap<>(), props);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Autowired @Qualifier("myDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.glqdlt.ex.**");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    private Properties additionalProperties() {
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
    public LazyConnectionDataSourceProxy lazyConnectionDataSourceProxy() {
        return new LazyConnectionDataSourceProxy(firstDataSource());
    }

    @Primary
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
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("1234");
        return hikariConfig;
    }

}
