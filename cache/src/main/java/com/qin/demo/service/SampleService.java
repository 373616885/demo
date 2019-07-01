package com.qin.demo.service;

import com.qin.demo.dao.SampleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "caffeine", cacheManager = "caffeineCacheManager")
public class SampleService {

    @Autowired
    private SampleDao dao;

    @Cacheable(key = "#root.method.name +'-service'")
    public String sample() {
        System.out.println("==========  Service run  ==========");
        return dao.sample();
    }


}
