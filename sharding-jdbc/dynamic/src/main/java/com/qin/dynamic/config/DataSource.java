package com.qin.dynamic.config;


import java.lang.annotation.*;

/**
 * @author qinjp
 * @date 2020/12/4
 */
// @Inherited 代理的时候 子类会继承这个注解
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

    String name() default DataSourceConfig.MASTER_7100;
}
