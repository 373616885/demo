package com.qin.spring.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 事务注解，需要此处添加该注解
@MapperScan("com.qin.spring.demo.mybatis.client") // 扫描的是mapper.xml中namespace指向值的包位置
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
