package com.zzsim.gz.airport.wma.controller;

import com.zzsim.gz.airport.common.base.OptResult;
import com.zzsim.gz.airport.sms.service.LingkaiSmsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjp
 * @date 2020/9/10
 */
@Slf4j
@RestController
@AllArgsConstructor
public class SmsController {

    private final LingkaiSmsService service;

    @GetMapping("sms/{mobile}")
    public OptResult<String> sendMobileCaptcha(@PathVariable("mobile") String mobile) {
        log.info("{} 发送短信验证码",mobile);
        service.sendMobileCaptcha(mobile);
        return OptResult.success();
    }

}
