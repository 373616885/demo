package com.zzsim.gz.airport.sms.service;

import com.zzsim.gz.airport.sms.domain.LingkaiSmsProperty;
import com.zzsim.gz.airport.sms.util.LingkaiSmsUtil;
import com.zzsim.gz.airport.sms.util.RandomNumUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>凌凯短信业务层接口实现类</p>
 *
 * @author qinjp
 * @date 2020/9/10
 */
@Slf4j
@AllArgsConstructor
public class LingkaiSmsService {

    private final LingkaiSmsProperty lingkaiSmsProperty;

    @SneakyThrows(Exception.class)
    public String sendMobileCaptcha(@NonNull String mobile) {

        Integer length = this.lingkaiSmsProperty.getCaptchaLength();
        // 产生随机数字短信验证码
        String captcha = RandomNumUtil.getRandomNum(length);

        System.out.println(lingkaiSmsProperty);

        String data = LingkaiSmsUtil.send(this.lingkaiSmsProperty.getUrl(),
            this.lingkaiSmsProperty.getUsername(),
            this.lingkaiSmsProperty.getPassword(),
            this.lingkaiSmsProperty.getTemplateCode(),
            this.lingkaiSmsProperty.getTemplateParam(),
            mobile, captcha);

        log.info("凌凯短信发送错误信息返回值：" + data);

        return data;
    }



}

