package com.qin.phone.config;

import com.qin.phone.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qinjp
 * @date 2019-07-18
 **/
@Configuration
@EnableConfigurationProperties(PhoneProperties.class)
@ConditionalOnClass(PhoneService.class)
@ConditionalOnWebApplication
//在application.properties配置"enable.qin.phone.enable"，对应的值为true
@ConditionalOnProperty(prefix = "enable.qin.phone", value = "enable", havingValue = "true",matchIfMissing = false)
public class PhoneAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(PhoneService.class)
    public PhoneService getPhoneService(PhoneProperties phoneProperties){
        return new PhoneService(phoneProperties);
    }

}
