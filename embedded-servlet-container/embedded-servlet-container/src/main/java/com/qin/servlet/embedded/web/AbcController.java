package com.qin.servlet.embedded.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AbcController {

    @RequestMapping("/a")
    public String abc() {
        return "a";
    }
}
