package com.qin.jdbc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SampleService {

    public String sample() {
        return "service";
    }

}
