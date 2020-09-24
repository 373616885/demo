package com.qin.tail.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping("/info")
    public String info() {
        return "info";
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello qinjp";
    }
}
