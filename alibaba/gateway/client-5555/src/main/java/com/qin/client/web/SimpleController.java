package com.qin.client.web;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author qinjp
 */
@Slf4j
@RestController
public class SimpleController {

    @Value("${server.port}")
    private String port;

    private static final LongAdder ADDER = new LongAdder();

    @SneakyThrows
    @GetMapping("/getPort")
    public String getPort() {
        ADDER.increment();
        Thread.sleep(150);
        log.warn("adder : {} getPort : {}", ADDER.intValue(), port);
        return port;
    }

}
