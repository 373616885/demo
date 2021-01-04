package com.qin.nacos.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjp
 * @date 2020/12/25
 */
@RestController
public class SampleController {

    @GetMapping("/hello")
    public String hello(){
        return "hello nacos";
    }

}
