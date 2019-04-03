package com.qin.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class TopicApplication {

    public static void main(String[] args) {
        SpringApplication.run(TopicApplication.class, args);
    }

    @Autowired
    Send send;

    @GetMapping("send")
    public void send(){
        send.send("AAAA","bind.a.qin");
        send.send("BBBB","bind.b.jie");
        send.send("CCCC","bind.a.b");
    }

}
