package com.qin.log.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleContller {

    @GetMapping("/sample")
    public String sample() {
        return "success";
    }

}
