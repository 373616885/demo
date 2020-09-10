package com.zzsim.gz.airport.sms.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取 短信 配置
 * @author qinjp
 * @date 2020/9/10
 */
@Data
@Component
@PropertySource("classpath:sms.properties")
@ConfigurationProperties(prefix = "sms.lingkai" , ignoreUnknownFields = false)
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
    private String templateCode;

    /**
     * 短信模板参数
     */
    private String templateParam;

    /**
     * 短信验证码有效时间，单位：秒（默认10分钟）
     */
    private Integer captchaTimeout = 600;

    /**
     * 短信验证码位数（默认6位）
     */
    private Integer captchaLength = 6;

    /**
     * 限制每小时次数（默认5次）
     */
    private Integer limitCountHour = 5;

    /**
     * 限制手机每天次数（默认20次）
     */
    private Integer limitCountEveryDay = 20;

}
