package com.qin.enable;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用事件客户端模块
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({Config.class, ClientConfiguration.class, UserImportSelector.class, EventImportBeanDefinitionRegistrar.class})
public @interface EnableClient {
}
