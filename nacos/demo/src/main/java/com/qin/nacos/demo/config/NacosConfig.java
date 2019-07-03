package com.qin.nacos.demo.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

/**
 * @author qinjp
 * @date 2019-07-02
 **/
@Configuration
@Getter
@NacosPropertySource(dataId = "nacos-config", groupId = "demo", autoRefreshed = true)
public class NacosConfig {

    @NacosValue(value = "${url}", autoRefreshed = true)
    private String url;

    @NacosValue(value = "${username}", autoRefreshed = true)
    private String username;

    @NacosValue(value = "${password}", autoRefreshed = true)
    private String password;

    @NacosValue(value = "${driverClassName}", autoRefreshed = true)
    private String driverClassName;


}
