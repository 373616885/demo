package com.qin.nacos.web;

import com.qin.nacos.pojo.NacosUsr;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SampleController {

    private final NacosUsr nacosUsr;

    @GetMapping("/sample")
    public NacosUsr sample(){
        return this.nacosUsr;
    }
}
