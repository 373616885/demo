package com.qin.nacos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qin.nacos.**.client")
public class NaocsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaocsApplication.class, args);
    }

}
