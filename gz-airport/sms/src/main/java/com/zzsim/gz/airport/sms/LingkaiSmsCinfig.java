package com.zzsim.gz.airport.sms;

import com.zzsim.gz.airport.sms.domain.LingkaiSmsProperty;
import com.zzsim.gz.airport.sms.service.LingkaiSmsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 凌凯短信配置类
 *
 * @author qinjp
 * @date 2020/9/10
 */
@Configuration
@Import({LingkaiSmsProperty.class, LingkaiSmsService.class})
public class LingkaiSmsCinfig {

}
