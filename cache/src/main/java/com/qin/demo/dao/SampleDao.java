package com.qin.demo.dao;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames ="redis",cacheManager = "redisCacheManager")
public class SampleDao {

    @Cacheable(key = "#root.method.name +'-dao'")
    public String sample() {
        System.out.println("==========  Repository run  ==========");
        return "Repository";
    }
}
