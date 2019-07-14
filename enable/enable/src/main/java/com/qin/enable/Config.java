package com.qin.enable;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class Config {

    @Value("${enable.qin.name}")
    private String name;

    @Value("${enable.qin.driverClassName}")
    private String driverClassName;

    @Value("${enable.qin.url}")
    private String url;

    @Value("${enable.qin.username}")
    private String username;

    @Value("${enable.qin.password}")
    private String password;

}
