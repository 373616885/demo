package com.zzsim.gz.airport.wma.service;

import com.zzsim.gz.airport.common.util.RandomNumUtil;
import com.zzsim.gz.airport.common.util.RegexUtils;
import com.zzsim.gz.airport.sms.service.LingkaiSmsService;
import lombok.AllArgsConstructor;
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
     * 校验手机是否存在
     *
     * @param mobile
     */
    public boolean checkMobileIsExist(String mobile) {
        // TODO
        return true;
    }

    /**
     * 发送短信
     *
     * @param mobile
     */
    public boolean send(String mobile) {
        // 校验手机号
        if (!RegexUtils.validateMobile(mobile)) {
            return false;
        }
        // 发送手机验证码
        //
        String captcha = RandomNumUtil.getRandomNum(4);
        lingkaiSmsService.send(mobile, captcha);


        return true;
    }

}
