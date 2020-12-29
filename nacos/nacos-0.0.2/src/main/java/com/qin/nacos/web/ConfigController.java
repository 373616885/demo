package com.qin.nacos.web;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjp
 * @date 2020/12/29
 */
@RestController
public class ConfigController {

    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @GetMapping("config/get")
    public boolean get() {
        return useLocalCache;
    }

}
