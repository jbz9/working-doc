package com.jiang.learn;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jbz
 */
@SpringBootApplication
@Slf4j
public class LearnApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(LearnApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("启动springboot");
    }
}
