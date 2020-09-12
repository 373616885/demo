package com.zzsim.gz.airport.common.util;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class CacheUtil {

	// 缓存接口这里是LoadingCache
	public static Cache<String, String> cache
	// CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
			= CacheBuilder.newBuilder()
					// 设置并发级别为8，并发级别是指可以同时写缓存的线程数
					.concurrencyLevel(8)
					// 在给定时间内没有被读/写访问，则清除
					//.expireAfterAccess(2,TimeUnit.SECONDS)
					// 在给定时间内没有被写访问（创建或覆盖），则清除 
					.expireAfterWrite(2, TimeUnit.SECONDS)
					// 设置缓存容器的初始容量为10
					.initialCapacity(100)
					// 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
					.maximumSize(100)
					.build();
	
	public static void main(String[] args) {
		CacheUtil.cache.put("qin", "测试");
		while (true) {
			System.out.println(CacheUtil.cache.getIfPresent("qin"));
			try {
				Thread.sleep(1100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
