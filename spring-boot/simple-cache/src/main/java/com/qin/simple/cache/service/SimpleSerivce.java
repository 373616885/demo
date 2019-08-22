package com.qin.simple.cache.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SimpleSerivce {


    @Cacheable("tmp")
    public String simple(){
        log.warn("==== simple serivce ====");
        return "serivce";
    }

}
