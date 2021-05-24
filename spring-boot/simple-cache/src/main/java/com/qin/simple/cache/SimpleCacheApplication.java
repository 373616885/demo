package com.qin.simple.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SimpleCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleCacheApplication.class, args);
    }
    /**
     * 体验缓存：
     *  1.开启注解缓存：  @EnableCaching
     *  2.标注缓存注解即可
     *      @Cacheable 主要针对方法配置，能够根据方法的请求参数对其结果进行缓存
     *      @CacheEvict 清空缓存
     *      @CachePut 保证方法被调用，又希望结果被缓存。
     *   默认使用的是SimpleCacheConfiguration 里面配置的
     *   ConcurrentMapCacheManager==ConcurrentMapCache
     *
     */

}
