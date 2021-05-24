package com.qin.register.controller;


import com.qin.register.domain.Operator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/hello")
    public String hello() {
        return "hello world ！" + port;
    }


    @GetMapping("get/operator")
    public Operator getOperator(String name) {
        Operator o = new Operator();
        o.setFullName(name);
        o.setOperatorId(Integer.valueOf(port));
        o.setUserName("qinjp");
        return o;
    }
}
