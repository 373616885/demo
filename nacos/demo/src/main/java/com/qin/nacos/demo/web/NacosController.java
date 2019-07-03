package com.qin.nacos.demo.web;

import com.qin.nacos.demo.config.NacosConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjp
 * @date 2019-07-02
 **/
@RestController
public class NacosController {

    @Autowired
    private NacosConfig nacosConfig;


    @GetMapping("/test")
    public String test(){
        return nacosConfig.getUsername();
    }

}
