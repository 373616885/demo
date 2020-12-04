package com.qin.dynamic.config;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author qinjp
 * @date 2020/12/4
 */
@Slf4j
@Aspect
@Component
@Order(value = 1) // 一定要在事务之前，事务默认Integer.MAX_VALUE
public class DataSourceAspect {


    @Before("@annotation(dataSource)")
    public void doBefore(DataSource dataSource) {
        if (DataSourceConfig.SLAVE.equals(dataSource.name())) {
            if (RandomUtil.randomBoolean()) {
                DataSourceContextHolder.setDataSourceType(DataSourceConfig.SLAVE_7200);
            } else {
                DataSourceContextHolder.setDataSourceType(DataSourceConfig.SLAVE_7300);
            }
            return;
        }
        DataSourceContextHolder.setDataSourceType(dataSource.name());
    }

    /**
     * After
     * 不管是否异常都执行，无法拿到结果
     * AfterRunning
     * 异常就不执行了，可以知道结果
     */
    @After("@annotation(DataSource)")
    public void doAfter() {
        DataSourceContextHolder.clearDataSourceType();
    }

}
