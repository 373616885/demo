package com.gradle.sample.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author qinjp
 **/
@RestController
public class SampleController {

    @RequestMapping("/say")
    public String say() {
        return "hello world!";
    }
}
