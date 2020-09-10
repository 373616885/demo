package com.zzsim.gz.airport.sms.service;

import com.zzsim.gz.airport.sms.domain.LingkaiSmsProperty;
import com.zzsim.gz.airport.sms.util.LingkaiSmsUtil;
import com.zzsim.gz.airport.sms.util.RandomNumUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>凌凯短信业务层接口实现类</p>
 *
 * @author qinjp
 * @date 2020/9/10
 */
@Slf4j
@Service
@AllArgsConstructor
public class LingkaiSmsService {

    private final LingkaiSmsProperty lingkaiSmsProperty;

    @SneakyThrows(Exception.class)
    public String sendMobileCaptcha(@NonNull String mobile) {

        Integer length = this.lingkaiSmsProperty.getCaptchaLength();
        // 产生随机数字短信验证码
        String captcha = RandomNumUtil.getRandomNum(length);
        // 凌凯发送短信
        String data = LingkaiSmsUtil.send(this.lingkaiSmsProperty.getUrl(),
                this.lingkaiSmsProperty.getUsername(),
                this.lingkaiSmsProperty.getPassword(),
                this.lingkaiSmsProperty.getTemplateCode(),
                this.lingkaiSmsProperty.getTemplateParam(),
                mobile, captcha);

        log.info("短信返回值：{}", data);

        return data;
    }

}

