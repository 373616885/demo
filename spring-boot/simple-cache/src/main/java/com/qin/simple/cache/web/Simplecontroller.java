package com.qin.simple.cache.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Simplecontroller {

    @GetMapping("/simple")
    public String simple(){
        return "success";
    }

}
