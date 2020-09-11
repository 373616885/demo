package com.zzsim.gz.airport.wma.service;

import com.zzsim.gz.airport.common.util.RegexUtils;
import com.zzsim.gz.airport.sms.service.LingkaiSmsService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

/**
 * @author qinjp
 * @date 2020/9/11
 */
@Service
@AllArgsConstructor
public class SmsService {

    private final LingkaiSmsService lingkaiSmsService;

    /**
     * 发送短信
     * @param mobile
     */
    public boolean send(String mobile) {
        // 校验手机号
        if (!RegexUtils.validateMobile(mobile)) {
            return false;
        }
        // 发送手机验证码
        ImmutablePair<Boolean, String> result = lingkaiSmsService.sendMobileCaptcha(mobile);




        return true;
    }

}
