package com.qin.direct;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DirectApplication {


    /**
     * Direct是RabbitMQ默认的交换机模式,也是最简单的模式.
     * 即创建消息队列的时候,指定一个BindingKey.
     * 当发送者发送消息的时候,指定对应的Key.
     * 当Key和消息队列的BindingKey一致的时候,
     * 消息将会被发送到该消息队列中.
     *
     * DirectExchange
     * rect 类型的行为是"先匹配, 再投送".
     * 即在绑定时设定一个 routing_key, 消息的routing_key完全匹配时,
     * 才会被交换器投送到绑定的队列中去。
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(DirectApplication.class, args);
    }

    @Autowired
    Send send;

    @Autowired
    Send2 send2;


    @GetMapping("send")
    public void send(){
        send.send("AAAA");
        send2.send2("BBBB");
        send.send("CCCC");
        send2.send2("DDDD");
//        send.send("EEEE");
    }




}
