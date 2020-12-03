package com.qin.docker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DockerApplication {

    public static void main(String[] args) {
		
		
		/**
		 * dockerfile-maven-plugin遇到巨坑，1.4.10版本windows10不能在pom.xml中使用
		 * 必须把Dockerfile放到项目根目录下
		 */
        SpringApplication.run(DockerApplication.class, args);
        log.warn("启动成功");
    }

}
