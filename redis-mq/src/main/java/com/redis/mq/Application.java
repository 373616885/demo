package com.redis.mq;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		// 这是一个发布和订阅模式
		ConfigurableApplicationContext cxt = SpringApplication.run(Application.class, args);
		// 生产和消费模式
		// 用lpush 和 brpop就可以
		// 对应的
//		String key = "key-1";
//		String value = "value-1";
//		StringRedisTemplate stringRedisTemplate = cxt.getBean(StringRedisTemplate.class);
//		stringRedisTemplate.boundListOps(key).leftPush(value);
//		while (true) {
//			System.out.println("key-1");
//			// 0 无限阻塞直到有数据为止
//			String result = stringRedisTemplate.boundListOps(key).rightPop(0, TimeUnit.SECONDS);
//			System.out.println(result);
//		}
	}

}
