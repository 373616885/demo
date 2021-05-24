package com.qin.knife4j.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjp
 */
@RestController
public class SampleController {

    @GetMapping("hello")
    public String sample(String name,String password) {
        return "hello world";
    }
}
