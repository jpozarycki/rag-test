package com.jpozarycki.ragtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class RagTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagTestApplication.class, args);
    }

}
