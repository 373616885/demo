package com.zzsim.gz.airport.wma.controller;

import com.zzsim.gz.airport.common.base.MsgSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 简单的测试类
 * @author qinjp
 * @date 2020/9/9
 */

@Slf4j
@RestController
public class SampleController {

    @GetMapping( value = "hello" , produces = MediaType.APPLICATION_JSON_VALUE)
    public String samlpe(){
        log.info("hello world");
        return "hello world" + MsgSource.getMsg("success");
    }

}
