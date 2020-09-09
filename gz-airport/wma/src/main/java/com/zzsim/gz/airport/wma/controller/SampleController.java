package com.zzsim.gz.airport.wma.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjp
 * @date 2020/9/9
 */
@RestController
public class SampleController {

    @GetMapping( value = "hello" , produces = MediaType.APPLICATION_JSON_VALUE)
    public String samlpe(){
        return "hello world";
    }

    @GetMapping( value = "he" , produces = MediaType.APPLICATION_JSON_VALUE)
    public void he(){
        System.out.println("hello so");
    }
}
