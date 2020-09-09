package com.zzsim.gz.airport.wma.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjp
 * @date 2020/9/9
 */
@RestController
public class SampleController {

    @GetMapping("hello")
    public String samlpe(){
        return "hello world";
    }
}
