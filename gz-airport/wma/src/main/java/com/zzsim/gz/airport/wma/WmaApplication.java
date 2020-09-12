package com.zzsim.gz.airport.wma;

import com.zzsim.gz.airport.cache.config.CaffeineConfig;
import com.zzsim.gz.airport.cache.config.RedisConfig;
import com.zzsim.gz.airport.sms.LingkaiSmsCinfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author qinjp
 * @date 2020/9/9
 */
@Import({LingkaiSmsCinfig.class , RedisConfig.class, CaffeineConfig.class})
@SpringBootApplication
public class WmaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WmaApplication.class, args);
    }

}
