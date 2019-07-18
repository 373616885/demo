package com.qin.phone.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qinjp
 * @date 2019-07-18
 **/
@Data
@ConfigurationProperties(prefix = "enable.qin.phone")
public class PhoneProperties {
    private String name;
    private Integer storage;
    private String money;
}


