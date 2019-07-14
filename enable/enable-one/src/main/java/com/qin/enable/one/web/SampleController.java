package com.qin.enable.one.web;

import com.qin.enable.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @Autowired
    private Client client;

    @GetMapping("/sample")
    public Client init() {
        return client;
    }

}
