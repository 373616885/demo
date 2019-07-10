package com.qin.jdbc.service;

import lombok.AllArgsConstructor;

/**
 * 省略构造器写法
 */
@AllArgsConstructor
public class ArgsConstructor {

    private final SampleService sampleService;

    public String args(){
        return sampleService.sample();
    }
}
