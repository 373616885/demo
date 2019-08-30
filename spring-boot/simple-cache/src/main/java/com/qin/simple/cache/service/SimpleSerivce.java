package com.qin.simple.cache.service;


import com.qin.simple.cache.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SimpleSerivce {

    /**
     * 将结果缓存，以后再要相同的数据 直接从缓存中寻找，不在调用方法
     * CacheManager 管理多个 Cache 组件 ，对缓存crud真正操作的事 Cache 每个缓存组件有自己唯一的名字
     * 几个属性：
     * cacheNames/value :指定缓存组价的名字；
     * key ：缓存数据使用的key；可以用它来指定。默认是使用方法参数的值  1-方法的返回值
     * 编写SpEL表达式  #id;#a0;#p0;#root.arg[0] 都是第一个参数 id
     * key = "#root.methodName+'['+#id+']'"    : key = simple[1]
     * <p>
     * keyGenerator: key的生成器；可以自己指定key的生成器的组件id
     * key/keyGenerator ： 二选一使用
     * <p>
     * cacheManager :指定缓存管理器;  或者 cacheResolver 指定获取解析器
     * <p>
     * condition :指定符合条件的情况下才缓存 condition = "#id>0"
     * condition = "#a0>1"  : 当一个参数大于1的时候才进行缓存
     * <p>
     * unless：否定缓存，在条件成立的情况下，方法的返回值不会被缓存，可以获取到结果进行判断
     * unless = "#result == null "  : 当结果= null的时候 不缓存
     * <p>
     * sync: 是否使用异步模式(该模式下 unless 将不支持了) 高并发的时候是有可能出现 多次计算 的情况 不符合缓存的目的
     * 原理：
     * 1、自动配置类；CacheAutoConfiguration
     * 2、缓存的配置类 debug = true 查看哪个配置生效
     * org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.GuavaCacheConfiguration
     * org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration【默认】
     * org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration
     * 3、哪个配置类默认生效：SimpleCacheConfiguration；
     * 4、给容器中注册了一个CacheManager：ConcurrentMapCacheManager
     * 5、可以获取和创建ConcurrentMapCache 类型的缓存组件；它的作用将数据保存在ConcurrentMap中；
     * <p>
     * 运行流程：
     *
     * @Cacheable： 1、方法运行之前，先去查询Cache（缓存组件），按照cacheNames指定的名字获取；
     * （CacheManager先获取相应的缓存），第一次获取缓存如果没有Cache组件会自动创建。
     * 2、去Cache中查找缓存的内容，使用一个key，默认就是方法的参数；
     * key是按照某种策略生成的；默认是使用keyGenerator生成的，默认使用SimpleKeyGenerator生成key；
     * SimpleKeyGenerator生成key的默认策略；
     * 如果没有参数；key=new SimpleKey()；
     * 如果有一个参数：key=参数的值
     * 如果有多个参数：key=new SimpleKey(params)；
     * 3、没有查到缓存就调用目标方法；
     * 4、将目标方法返回的结果，放进缓存中
     * @Cacheable标注的方法执行之前先来检查缓存中有没有这个数据，默认按照参数的值作为key去查询缓存， 如果没有就运行方法并将结果放入缓存；以后再来调用就可以直接使用缓存中的数据；
     * <p>
     * 核心：
     * 1）、使用CacheManager【ConcurrentMapCacheManager】按照名字得到Cache【ConcurrentMapCache】组件
     * 2）、key使用keyGenerator生成的，默认是SimpleKeyGenerator
     */
    @Cacheable(cacheNames = "tmp", key = "#root.methodName+'[qinjp]'")
    public String simple(User user) {
        log.warn("==== simple serivce " + user.getName() + "====");
        return "serivce";
    }


    /**
     * @CachePut 即调用方法，又更新缓存中的数据( 一定要和 @Cacheable中的key保持一致)
     * 运行时机：
     * 1.先调用目标方法，
     * 2.在将方法的结果保存到缓存中
     * <p>
     * 测试步骤
     * 1.先查询1号员工，查到的结果放到缓存中
     * 2.以后查询还有之前的结果
     * 3.更新1号员工的信息
     * 4.查询到1号员工?
     * 查询到的还是跟新之前的1号员工。。。。。为什么？ 因为没有指定key值默认是以参数名称绑定数据的
     * 解决办法： 指定key 和查询时使用的key保持一致
     */
    @CachePut(value = "tmp", key = "'simple[qinjp]'")
    public String updateById(User user) {
        System.out.println("update: " + user.getId());
        return "update";
    }


    /**
     * @CacheEvict :清空缓存
     * value = "emp"  缓存的名字
     * key = "#id"   缓存的id
     * allEntries = true  指定清空这个缓存中的所有数据
     * beforeInvocation = true 再方法调用之前清除缓存，（默认是false）
     * 防止方法异常无法清楚缓存
     */
    @CacheEvict(value = "tmp", /*key = "'simple[qinjp]'",*/ allEntries = true, beforeInvocation = true)
    public void deleteEmp(Integer id) {
        System.out.println("delete: " + id);
    }


    // @Caching 定义复杂的缓存规则
    @Caching(
            cacheable = {
                    @Cacheable(value = "emp", key = "#lastName")
            },
            put = {
                    @CachePut(value = "emp", key = "#result.id"),
                    @CachePut(value = "emp", key = "#result.email")
            }
    )
    public User getEmpByLastName(String lastName) {
        return new User();
    }

}
