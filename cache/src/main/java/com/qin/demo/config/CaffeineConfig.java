package com.qin.demo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CaffeineConfig {

    @Value("${spring.cache.caffeine.spec}")
    private String caffeineSpec;


    @Bean(name = "caffeineCacheManager")
    public CacheManager cacheManagerWithCaffeineFromSpec(){
        CaffeineSpec spec = CaffeineSpec.parse(caffeineSpec);
        Caffeine caffeine = Caffeine.from(spec);
        //此方法等同于上面from(spec)
//        Caffeine caffeine = Caffeine.from(caffeineSpec);

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheNames(getNames());
        return cacheManager;
    }

    private static List<String> getNames(){
        List<String> names = new ArrayList<>(2);
        names.add("caffeine");
        names.add("caffeineTwo");
        return names;
    }

}
