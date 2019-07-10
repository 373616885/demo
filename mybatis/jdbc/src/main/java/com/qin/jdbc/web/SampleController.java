package com.qin.jdbc.web;

import com.qin.jdbc.service.ArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @Autowired
    ArgsConstructor argsConstructor;

    @GetMapping("/sample")
    public String init(){
        return argsConstructor.args();
    }
}
