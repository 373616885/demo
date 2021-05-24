package com.zzsim.gz.airport.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.google.common.collect.ImmutableList;
import com.zzsim.gz.airport.cache.base.CacheConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *  本地缓存配置类
 *
 * @author qinjp
 * @date 2020/9/12
 */
@Configuration
public class CaffeineConfig {

    @Value("${spring.cache.caffeine.spec:initialCapacity=100,maximumSize=500,expireAfterWrite=1800s}")
    private String caffeineSpec;


    @Bean(name = "caffeineCacheManager")
    public CacheManager cacheManagerWithCaffeineFromSpec(){
        CaffeineSpec spec = CaffeineSpec.parse(caffeineSpec);
        Caffeine<Object, Object> caffeine = Caffeine.from(spec);
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheNames(getNames());
        return cacheManager;
    }


    private static List<String> getNames(){
        return ImmutableList.of(CacheConstant.KEY_PREFIX);
    }

}
