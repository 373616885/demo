package com.zzsim.gz.airport.wma.service.sms;

import com.zzsim.gz.airport.cache.component.RedisLimit;
import com.zzsim.gz.airport.common.util.RandomNumUtil;
import com.zzsim.gz.airport.sms.service.LingkaiSmsService;
import com.zzsim.gz.airport.wma.domain.sms.SmsCaptchaProperty;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author qinjp
 * @date 2020/9/11
 */
@Slf4j
@Service
@AllArgsConstructor
public class SmsService {

    private final LingkaiSmsService lingkaiSmsService;

    private final SmsCaptchaProperty smsCaptchaProperty;

    private final RedisLimit redisLimit;

    /**
     * 发送验证码规则限制
     * 规则：
     * 限制每小时发送次数
     * 限制手机每天次数
     */
    public boolean sendLimit(String mobile) {
        // 限制每小时发送次数
        boolean limitCountHour = redisLimit.limit("limit.count.hour:" + mobile, 3600, smsCaptchaProperty.getLimitCountHour());
        if (!limitCountHour) {
            return false;
        }
        // 限制手机24小时
        return redisLimit.limit("limit.count.day:" + mobile, 86400, smsCaptchaProperty.getLimitCountDay());
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
        String captcha = RandomNumUtil.getRandomNum(smsCaptchaProperty.getLength());

        //lingkaiSmsService.send(mobile, captcha);

        log.info("手机: {}  验证码: {}  ", mobile, captcha);

        // 记录日志
        log(mobile, captcha);

        return true;
    }

    // 不打开事务自动提交
    private void log(String mobile, String captcha) {

    }


}
