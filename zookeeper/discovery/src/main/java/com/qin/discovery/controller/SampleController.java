package com.qin.discovery.controller;


import com.qin.discovery.domain.Operator;
import com.qin.discovery.service.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SampleController {

    @Autowired
    private DiscoveryService discoveryService;

    @Value("${server.port}")
    private String port;

    @GetMapping("/hello")
    public String hello() {
        return "hello world ！" + port;
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("get/operator")
    public Operator getOperator(String name) {

        String host = discoveryService.discovery();
        System.out.println(host);
        Operator o = restTemplate.getForObject("http://" + host + "/get/operator?name=" + name, Operator.class);
        return o;
    }
}
