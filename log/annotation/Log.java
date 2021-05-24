package com.zzsim.gz.airport.web.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qinjp
 * @date 2020/10/13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 操作类型
     */
    String type() default "未知";

    /**
     * 操作内容
     */
    String operation();

}
