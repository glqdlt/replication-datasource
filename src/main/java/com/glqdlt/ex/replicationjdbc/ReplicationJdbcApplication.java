package com.glqdlt.ex.replicationjdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootApplication
public class ReplicationJdbcApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;


    public static void main(String[] args) {
        SpringApplication.run(ReplicationJdbcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.submit(() -> {
            while (true) {
                log.info("master : {}", userService.findByMaster().toString());
                Thread.sleep(1000);
            }
        });
        pool.submit(() -> {
            while (true) {
                log.info("slave : {}", userService.findBySlave().toString());
                Thread.sleep(10);
            }
        });

    }
}

