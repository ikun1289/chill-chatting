package com.ttnm.chillchatting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication
@EnableAsync
public class ChillchattingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChillchattingApplication.class, args);
    }

}
