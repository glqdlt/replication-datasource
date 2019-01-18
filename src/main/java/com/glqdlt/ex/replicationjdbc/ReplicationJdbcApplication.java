package com.glqdlt.ex.replicationjdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootApplication(exclude = JtaAutoConfiguration.class)
public class ReplicationJdbcApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ReplicationJdbcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

}

