package com.qin.simple.cache.web;

import com.qin.simple.cache.bean.User;
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

    @GetMapping("/get")
    public String get(User user) {
        String result = simpleSerivce.simple(user);
        log.warn(result);
        return result;
    }

    @GetMapping("/update")
    public String update(User user) {
        return simpleSerivce.updateById(user);
    }

}
