package com.qin.nacos.web;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author qinjp
 * @date 2020/12/29
 */
@RestController
public class DiscoveryController {

    @NacosInjected
    private NamingService namingService;

    @GetMapping("discovery/get")
    public List<Instance> get() throws NacosException {
        return namingService.getAllInstances("dev_service", "dev_group");
    }

    private LongAdder adder = new LongAdder();

    @GetMapping("register/instance")
    public List<Instance> registerInstance() throws NacosException {
        adder.increment();
        Instance instance = new Instance();
        instance.setIp("55.55.55.51");
        int port = 9911 + adder.intValue();
        instance.setPort(port);
        instance.setHealthy(true);
        instance.setWeight(2.0);
        Map<String, String> instanceMeta = new HashMap<>();
        instanceMeta.put("instanceMeta", "qinjp");
        instance.setMetadata(instanceMeta);

        // 这段好像没有用
//        Service service = new Service("nacos");
//        service.setAppName("app_name");
//        service.setGroupName("group_name");
//        service.setProtectThreshold(0.8F);
//        Map<String, String> serviceMeta = new HashMap<>();
//        serviceMeta.put("serviceMeta", "qinjp");
//        service.setMetadata(serviceMeta);

        //instance.setServiceName("nacos");

        namingService.registerInstance("nacos", "group_name", instance);

        return namingService.getAllInstances("nacos", "group_name");
    }

    @GetMapping("select/one/healthy/instance")
    public Instance selectOneHealthyInstance() throws NacosException {
        return namingService.selectOneHealthyInstance("nacos","group_name");
    }


}
