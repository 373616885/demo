package com.qin.standard.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjp
 */
@Slf4j
@RestController
public class SimpleController {

    @GetMapping("/simple")
    public String simple() {
        log.warn("simple");
        return "simple";
    }

}
