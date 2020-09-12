package com.zzsim.gz.airport.wma.domain.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qinjp
 * @date 2020/9/12
 */
@Data
@ConfigurationProperties(prefix = "sms.captcha", ignoreUnknownFields = false)
public class SmsCaptchaProperty {

    /**
     * 短信验证码有效时间，单位秒
     */
    private Integer timeout;

    /**
     * 短信验证码位数
     */
    private Integer length;

    /**
     * 限制每小时发送次数
     */
    private Integer limitCountHour;

    /**
     * 限制手机每天次数
     */
    private Integer limitCountDay;

}
