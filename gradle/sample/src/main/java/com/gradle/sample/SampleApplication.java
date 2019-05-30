package com.gradle.sample;

import com.gradle.sample.domain.Dog;
import com.gradle.sample.domain.Student;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableTransactionManagement
@MapperScan("com.gradle.*.mybatis.client")
@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SampleApplication.class, args);
//        Dog dog = context.getBean(Dog.class);
//        System.out.println(dog);
        Student student = context.getBean(Student.class);
        System.out.println(student.getName());
    }

}
