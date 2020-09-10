package com.zzsim.gz.airport.sms.service;

import com.zzsim.gz.airport.sms.domain.LingkaiSmsProperty;
import com.zzsim.gz.airport.sms.util.RandomNumUtil;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 凌凯短信业务类
 *
 * @author qinjp
 * @date 2020/9/10
 */
@Slf4j
@Service
@AllArgsConstructor
public class LingkaiSmsService {

    private final LingkaiSmsProperty lingkaiSmsProperty;

    /**
     * 发送短信验证码
     * 返回值大于零则代表成功
     * @param mobile 手机号
     */
    @SneakyThrows(Exception.class)
    public String sendMobileCaptcha(@NonNull String mobile) {

        Integer length = this.lingkaiSmsProperty.getCaptchaLength();
        // 产生随机数字短信验证码
        String captcha = RandomNumUtil.getRandomNum(length);
        // 凌凯发送短信
        String data = send(this.lingkaiSmsProperty.getUrl(),
                this.lingkaiSmsProperty.getUsername(),
                this.lingkaiSmsProperty.getPassword(),
                this.lingkaiSmsProperty.getTemplateCode(),
                this.lingkaiSmsProperty.getTemplateParam(),
                mobile, captcha);

        log.info("短信返回值：{}", data);

        return data;
    }


    /**
     * 发送短信，成功返回流水号(只支持一个参数)
     *
     * @param url           请求地址
     * @param username      用户名
     * @param password      密码
     * @param templateCode  短信模板
     * @param templateParam 短信模板参数
     * @param phoneNumbers  手机号(多个逗号分隔)
     * @param captcha       短信验证码
     */
    @SneakyThrows(Exception.class)
    private String send(String url, String username, String password, String templateCode, String templateParam, String phoneNumbers, String captcha) {
        templateCode = templateCode.replaceAll(templateParam, captcha);
        String content = URLEncoder.encode(templateCode, "GBK");
        String path = url +
                "?CorpID=" + username +
                "&Pwd=" + password +
                "&Mobile=" + phoneNumbers +
                "&Content=" + content;

        log.info("手机: {} ：内容: {}", phoneNumbers, content);

        @Cleanup
        InputStreamReader isr = new InputStreamReader(new URL(path).openStream(), StandardCharsets.UTF_8);

        @Cleanup
        BufferedReader br = new BufferedReader(isr);

        return br.readLine();
    }

}

