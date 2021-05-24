package com.redis.mq;

import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

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

//	@Bean
//	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//		ObjectMapper om = new ObjectMapper();
//		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		jackson2JsonRedisSerializer.setObjectMapper(om);
//		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//		template.setConnectionFactory(redisConnectionFactory);
//		template.setKeySerializer(jackson2JsonRedisSerializer);
//		template.setValueSerializer(jackson2JsonRedisSerializer);
//		template.setHashKeySerializer(jackson2JsonRedisSerializer);
//		template.setHashValueSerializer(jackson2JsonRedisSerializer);
//		template.afterPropertiesSet();
//		return template;
//	}

}
