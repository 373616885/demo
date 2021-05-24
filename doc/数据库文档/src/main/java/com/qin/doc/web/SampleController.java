package com.qin.doc.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
public class SampleController {

    @GetMapping("hello")
    public String sample(String name,String password) {
        return "hello world";
    }
}
