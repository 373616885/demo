package com.zzsim.gz.airport.cache.config;

import com.zzsim.gz.airport.cache.component.RedisLimit;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * redis配置类
 *
 * @author qinjp
 * @date 2020/9/12
 */
@Configuration
@Import(RedisLimit.class)
public class RedisConfig {

}
