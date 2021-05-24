package com.qin.docker.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author qinjp
 * @date 2020/11/25
 */
@RestController
public class SimpleController {

    @GetMapping("hello")
    public String hello() {
        return "hello world";
    }

    @GetMapping("time")
    public String time() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @GetMapping("date")
    public String date() {
        return ZonedDateTime.now().toString().toString();
    }

}
