package com.qin.dynamic.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author qinjp
 * @date 2020/12/4
 */
@Configuration
public class DataSourceConfig {

    static final String MASTER_7100 = "master_7100";
    static final String SLAVE = "slave";
    static final String SLAVE_7200 = "slave_7200";
    static final String SLAVE_7300 = "slave_7300";

    @Bean(name = MASTER_7100)
    @ConfigurationProperties(prefix = "spring.datasource.master7100")
    public HikariDataSource master7100() {
        return new HikariDataSource();
    }

    @Bean(name = SLAVE_7200)
    @ConfigurationProperties(prefix = "spring.datasource.slave7200")
    public HikariDataSource slave7200() {
        return new HikariDataSource();
    }

    @Bean(name = SLAVE_7300)
    @ConfigurationProperties(prefix = "spring.datasource.slave7300")
    public HikariDataSource slave7300() {
        return new HikariDataSource();
    }

    @Bean(name = "dynamicDataSource")
    @Primary
    @DependsOn({MASTER_7100, SLAVE_7200, SLAVE_7300})
    public AbstractRoutingDataSource dynamicDataSource(@Qualifier(MASTER_7100) HikariDataSource master7100,
                                                       @Qualifier(SLAVE_7200) HikariDataSource slave7200,
                                                       @Qualifier(SLAVE_7300) HikariDataSource slave7300) {
        AbstractRoutingDataSource dynamicDataSource = new AbstractRoutingDataSource() {
            @SneakyThrows
            @Override
            protected Object determineCurrentLookupKey() {
                // 如果有事务就是操作主数据库 -- 获取连接在开启事务之前 这里判断是错误的
                //if (TransactionSynchronizationManager.isActualTransactionActive()) {
                //    return MASTER_7100;
                //}
                Field currentProxy = AopContext.class.getDeclaredField("currentProxy");
                currentProxy.setAccessible(true);
                ThreadLocal<Object> o = (ThreadLocal<Object>)currentProxy.get(null);
                if(Objects.isNull(o.get())){
                    System.out.println(o.get());
                }
                // 结合这个使用
                // @EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
                System.out.println(AopContext.currentProxy());

                System.out.println(AopUtils.getTargetClass(AopContext.currentProxy()).isAnnotationPresent(Transactional.class));

                return DataSourceContextHolder.getDataSourceType();
            }
        };

        Map<Object, Object> dataSourceMap = new HashMap<>(3);
        dataSourceMap.put(MASTER_7100, master7100);
        dataSourceMap.put(SLAVE_7200, slave7200);
        dataSourceMap.put(SLAVE_7300, slave7300);
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        // 默认数据库
        dynamicDataSource.setDefaultTargetDataSource(master7100());

        return dynamicDataSource;
    }
}
