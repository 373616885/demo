package com.zzsim.gz.airport.wma.web;

import com.zzsim.gz.airport.common.base.MsgSource;
import com.zzsim.gz.airport.common.base.OptResult;
import com.zzsim.gz.airport.common.util.RegexUtils;
import com.zzsim.gz.airport.wma.domain.sms.SmsSendDTO;
import com.zzsim.gz.airport.wma.service.sms.SmsService;
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

    private final SmsService smsService;

    @PostMapping("sms/mobile/captcha")
    public OptResult<String> sendMobileCaptcha(@Validated SmsSendDTO mobile) {
        // 校验手机号
        if (!RegexUtils.validateMobile(mobile.getMobile())) {
            return OptResult.fail(MsgSource.getMsg("incorrect.format.o.mobile.phone"));
        }
        log.info("{} 发送短信验证码",mobile);
        smsService.send(mobile.getMobile());
        return OptResult.success();
    }

}
