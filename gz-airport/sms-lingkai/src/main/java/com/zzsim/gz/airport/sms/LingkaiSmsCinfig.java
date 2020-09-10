package com.zzsim.gz.airport.sms;

import com.zzsim.gz.airport.sms.domain.LingkaiSmsProperty;
import com.zzsim.gz.airport.sms.service.LingkaiSmsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author qinjp
 * @date 2020/9/10
 */
@Configuration
@Import(LingkaiSmsProperty.class)
public class LingkaiSmsCinfig {

    @Bean
    public LingkaiSmsService lingkaiSmsService(LingkaiSmsProperty lingkaiSmsProperty){
        return new LingkaiSmsService(lingkaiSmsProperty);
    }
}
