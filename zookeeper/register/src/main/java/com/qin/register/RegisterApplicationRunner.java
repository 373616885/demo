package com.qin.register;

import com.qin.register.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * 这里通过设定value的值来指定执行顺序
 */
@Component
@Order(value = 1)
public class RegisterApplicationRunner implements ApplicationRunner {

    @Value("${server.port}")
    private String port;

    @Autowired
    private RegisterService registerService;

    @Override
    public void run(ApplicationArguments var1) throws Exception {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            registerService.register(ip + ":" + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
