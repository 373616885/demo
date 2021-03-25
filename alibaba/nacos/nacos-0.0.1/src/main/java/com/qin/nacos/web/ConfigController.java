package com.qin.nacos.web;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author qinjp
 * @date 2020/12/28
 */
@RestController
public class ConfigController {

    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @NacosInjected
    private NamingService namingService;

    @GetMapping("config/get")
    public boolean get() {
        return this.useLocalCache;
    }

    @GetMapping("discovery/get")
    public List<Instance> getServices() throws NacosException {
        return namingService.getAllInstances("nacos","qin");
    }

}
