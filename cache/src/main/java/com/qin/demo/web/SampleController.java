package com.qin.demo.web;

import com.qin.demo.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class SampleController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("request/safe")
    public String requestSafe(HttpServletRequest req) {
        stringRedisTemplate.opsForValue().set("qin", "373616885");
        System.out.println(req.getClass().getName());
        System.out.println(req.getParameter("name"));
        System.out.println(request.getClass().getName());
        System.out.println(request.getParameter("name"));
        System.out.println("session :" + session.getClass().getName());
        System.out.println("session :" + session.getAttributeNames());
        return "SUCCESS";
    }

    @Autowired
    private SampleService sampleService;

    @GetMapping("cache")
    public String cache() {
        String cache = sampleService.sample();
        return cache;
    }

}
