package com.apiorbit.lovableclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LovableCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(LovableCloneApplication.class, args);
    }

}
