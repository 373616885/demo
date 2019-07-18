package com.qin.enable.one.web;

import com.qin.enable.Client;
import com.qin.phone.service.PhoneService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class SampleController {

    private final Client client;

    private final PhoneService phoneService;

    @GetMapping("/sample")
    public Client init() {
        return client;
    }

    @GetMapping("/phone")
    public String phoneProperties() {
        return phoneService.phone();
    }

}
