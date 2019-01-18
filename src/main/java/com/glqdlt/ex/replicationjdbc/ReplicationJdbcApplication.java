package com.glqdlt.ex.replicationjdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootApplication
public class ReplicationJdbcApplication implements CommandLineRunner {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DataSourceChanger dataSourceChanger;

    @Autowired
    @Qualifier("secondDataSource")
    private DataSource secondSource;


    @Autowired
    @Qualifier("firstDataSource")
    private DataSource firstSource;


    public static void main(String[] args) {
        SpringApplication.run(ReplicationJdbcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            while (true) {
                log.info(userRepo.findAll().toString());
                Thread.sleep(1000);
            }
        });
        executorService.submit(() -> {
            while (true) {
                Thread.sleep(2000);
                int dd = LocalDateTime.now().getSecond();
                if (dd % 3 == 0) {
                    dataSourceChanger.changeDataSource(secondSource);
                }else{
                    dataSourceChanger.changeDataSource(firstSource);
                }
            }
        });
    }
}

