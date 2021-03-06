package com.qin.gateway.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.util.Properties;

@Slf4j
@Configuration
public class GataWayConfig {

    public static final String DATA_ID = "gateway";
    public static final String GROUP = "routes";
    public static final long TIME_OUT_MS = 5000;

    @Bean
    @SneakyThrows
    public ConfigService configService() {
        Properties properties = new Properties();
        properties.put("serverAddr", "127.0.0.1:8848");
        properties.setProperty("namespace", "dev");
        properties.setProperty("username", "nacos");
        properties.setProperty("password", "373616885");
        return NacosFactory.createConfigService(properties);
    }

}
