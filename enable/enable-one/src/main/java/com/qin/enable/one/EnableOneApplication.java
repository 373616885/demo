package com.qin.enable.one;

import com.qin.enable.EnableClient;
import com.qin.enable.Event;
import com.qin.enable.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@EnableClient
@SpringBootApplication
public class EnableOneApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(EnableOneApplication.class, args);
        System.out.println(run.getBean(User.class).toString());
        System.out.println(run.getBean(Event.class).toString());
    }

}
