package com.qin.simple.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SimpleCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleCacheApplication.class, args);
    }

}
