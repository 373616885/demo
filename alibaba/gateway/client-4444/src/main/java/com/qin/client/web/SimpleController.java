package com.qin.client.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjp
 */
@Slf4j
@RestController
public class SimpleController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/getPort")
    public String getPort() {
        log.warn("getPort : {}", port);
        return port;
    }

}
