package com.qin.dynamic.config;

import cn.hutool.core.util.RandomUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qinp
 */
@Configuration
public class DataSourceConfig {

    public static final String MASTER_7100 = "master_7100";
    public static final String SLAVE_7200 = "slave-7200";
    public static final String SLAVE_7300 = "slave-7300";

    @Bean(name = MASTER_7100)
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public HikariDataSource master7100() {
        return new HikariDataSource();
    }

    @Bean(name = SLAVE_7200)
    @ConfigurationProperties(prefix = "spring.datasource.slave-7200")
    public HikariDataSource slave7200() {
        return new HikariDataSource();
    }

    @Bean(name = SLAVE_7300)
    @ConfigurationProperties(prefix = "spring.datasource.slave-7300")
    public HikariDataSource slave7300() {
        return new HikariDataSource();
    }

    @Bean
    public AbstractRoutingDataSource dynamicDataSource() {
        AbstractRoutingDataSource dynamicDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                // 如果有事务就是操作主数据库
                if (TransactionSynchronizationManager.isActualTransactionActive()) {
                    return MASTER_7100;
                }
                return RandomUtil.randomBoolean() ? SLAVE_7200 : SLAVE_7300;
            }
        };

        Map<Object, Object> dataSourceMap = new HashMap<>(3);
        dataSourceMap.put(MASTER_7100, master7100());
        dataSourceMap.put(SLAVE_7200, slave7200());
        dataSourceMap.put(SLAVE_7300, slave7300());
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        // 默认数据库
        dynamicDataSource.setDefaultTargetDataSource(master7100());

        return dynamicDataSource;
    }
}
