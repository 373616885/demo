package com.qin.stream.server.controller;


import com.qin.stream.server.service.SinkSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @Autowired
    private SinkSender sinkSender;

    @RequestMapping("/send")
    public String send(@RequestParam String msg){
        sinkSender.send(msg);
        return "success";
    }

}
