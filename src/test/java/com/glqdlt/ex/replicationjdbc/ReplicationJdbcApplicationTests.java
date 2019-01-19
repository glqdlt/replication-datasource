package com.glqdlt.ex.replicationjdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReplicationJdbcApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void log() throws Exception {

        ExecutorService pool = Executors.newFixedThreadPool(4);
        pool.execute(() -> {
            while (true) {
                log.info(String.format("Result ==> %s", userService.findUsersByEm().toString()));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        pool.awaitTermination(60, TimeUnit.SECONDS);
    }


    @Test
    public void log2() throws Exception {

        ExecutorService pool = Executors.newFixedThreadPool(4);
        pool.execute(() -> {
            while (true) {
                log.info(String.format("Result ==> %s", userService.findUsersByJpaRepo().toString()));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        pool.awaitTermination(60, TimeUnit.SECONDS);
    }
}

