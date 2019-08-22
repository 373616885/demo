package com.qin.simple.cache.web;

import com.qin.simple.cache.service.SimpleSerivce;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class Simplecontroller {

    private final SimpleSerivce simpleSerivce;

    @GetMapping("/simple")
    public String simple(){
        String result = simpleSerivce.simple();
        log.warn(result);
        return "success";
    }

}
