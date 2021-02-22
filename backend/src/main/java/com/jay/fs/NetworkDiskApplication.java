package com.jay.fs;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@EnableRabbit
@SpringBootApplication
public class NetworkDiskApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetworkDiskApplication.class, args);
    }

}
