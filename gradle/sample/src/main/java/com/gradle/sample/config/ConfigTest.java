package com.gradle.sample.config;

import com.gradle.sample.domain.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qinjp
 * @date 2019-05-29
 **/
public class ConfigTest {

    @Configuration
    static class StudentConfig {
        @Bean
        public Student createStudent(){
            Student student = new Student();
            student.setName("student");
            return student;
        }
    }
}
