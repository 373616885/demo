package com.zzsim.gz.airport.sms.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取 短信 配置
 *
 * @author qinjp
 * @date 2020/9/10
 */
@Data
@Component
@PropertySource("classpath:sms.properties")
@ConfigurationProperties(prefix = "sms.lingkai")
public class LingkaiSmsProperty {

    /**
     * 请求路径
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 短信模板
     */
    private String template;


}
