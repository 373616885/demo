package com.zzsim.gz.airport.wma.service.sms;

import com.zzsim.gz.airport.common.util.RandomNumUtil;
import com.zzsim.gz.airport.sms.service.LingkaiSmsService;
import com.zzsim.gz.airport.wma.domain.sms.SmsCaptchaProperty;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author qinjp
 * @date 2020/9/11
 */
@Service
@AllArgsConstructor
public class SmsService {

    private final StringRedisTemplate stringRedisTemplate;

    private final LingkaiSmsService lingkaiSmsService;

    private final SmsCaptchaProperty smsCaptchaProperty;


    /**
     * 发送验证码规则限制
     * 规则：
     * 限制每小时发送次数
     * 限制手机每天次数
     */
    public boolean sendBefore(String mobile) {
        // TODO
        return true;
    }

    /**
     * 发送验证码存规则配置
     * 规则：
     *  限制每小时发送次数
     *  限制手机每天次数
     */
    public void sendAfter(String mobile) {
        // TODO
    }

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

        // 发送手机验证码
        //
        String captcha = RandomNumUtil.getRandomNum(4);
        lingkaiSmsService.send(mobile, captcha);


        return true;
    }

}
