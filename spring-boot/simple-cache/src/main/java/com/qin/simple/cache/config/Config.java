package com.qin.simple.cache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

//@Configuration
public class Config {

    /**
     * 自定义redis key值生成策略
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                // json 序列化更好
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    @Value("${spring.cache.caffeine.spec}")
    private String caffeineSpec;


//    @Bean(name = "caffeineCacheManager")
//    public CacheManager cacheManagerWithCaffeineFromSpec(){
//        CaffeineSpec spec = CaffeineSpec.parse(caffeineSpec);
//        Caffeine caffeine = Caffeine.from(spec);
//        //此方法等同于上面from(spec)
//        //Caffeine caffeine = Caffeine.from(caffeineSpec);
//
//        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
//        cacheManager.setCaffeine(caffeine);
//        cacheManager.setCacheNames(getNames());
//        return cacheManager;
//    }
//
//    private static List<String> getNames(){
//        List<String> names = new ArrayList<>(2);
//        names.add("caffeine");
//        names.add("caffeineTwo");
//        return names;
//    }
}
