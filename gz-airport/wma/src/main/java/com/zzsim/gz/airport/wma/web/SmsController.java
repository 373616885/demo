package com.zzsim.gz.airport.wma.web;

import com.zzsim.gz.airport.common.base.OptResult;
import com.zzsim.gz.airport.wma.domain.SmsSendDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjp
 * @date 2020/9/10
 */
@Slf4j
@RestController
@AllArgsConstructor
public class SmsController {

    @PostMapping("sms/mobile/captcha")
    public OptResult<String> sendMobileCaptcha(@Validated SmsSendDTO mobile) {
        log.info("{} 发送短信验证码",mobile);
        return OptResult.success();
    }

}
