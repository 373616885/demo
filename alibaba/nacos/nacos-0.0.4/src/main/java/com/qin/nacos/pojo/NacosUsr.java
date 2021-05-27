package com.qin.nacos.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class NacosUsr {

    @Value("${usr}")
    private String usr;

    @Value("${localusr}")
    private String localusr;

    @Value("${sharedusr}")
    private String sharedusr;
}
