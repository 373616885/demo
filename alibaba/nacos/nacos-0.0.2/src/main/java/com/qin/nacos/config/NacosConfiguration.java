package com.qin.nacos.config;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;
import org.springframework.context.annotation.Configuration;

/**
 * @author qinjp
 * @date 2020/12/29
 */
@Configuration
@EnableNacosConfig(readConfigTypeFromDataId = false, globalProperties = @NacosProperties(serverAddr = "47.100.185.77:8848", namespace = "dev", username = "nacos", password = "nacos"))
@NacosPropertySource(name = "dev", dataId = "dev_data", groupId = "dev_group", type = ConfigType.YAML, autoRefreshed = true)
@EnableNacosDiscovery(globalProperties = @NacosProperties(serverAddr = "47.100.185.77:8848", namespace = "dev", username = "nacos", password = "nacos"))
public class NacosConfiguration {

}
