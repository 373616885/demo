package com.zzsim.gz.airport.sms.service;

import com.zzsim.gz.airport.common.util.RandomNumUtil;
import com.zzsim.gz.airport.sms.domain.LingkaiSmsProperty;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
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
     * ImmutablePair
     *  L 是否发送成功
     *  R 发送短信内容
     * @param mobile 手机号
     */
    @SneakyThrows(Exception.class)
    public ImmutablePair<Boolean, String> sendMobileCaptcha(@NonNull String mobile) {

        Integer length = this.lingkaiSmsProperty.getCaptchaLength();
        // 产生随机数字短信验证码
        String captcha = RandomNumUtil.getRandomNum(length);
        // 发送内容
        String content = this.lingkaiSmsProperty.getTemplateCode().replaceAll(this.lingkaiSmsProperty.getTemplateParam(), captcha);
        // 凌凯发送短信
        String data = send(this.lingkaiSmsProperty.getUrl(),
                this.lingkaiSmsProperty.getUsername(),
                this.lingkaiSmsProperty.getPassword(),
                content,
                mobile);

        log.info("手机: {}  内容: {}  返回值：{}", mobile, content, data);

        // 判断是否发送成功
        Boolean success = StringUtils.isNotBlank(data) && Integer.parseInt(data) > 0;

        return ImmutablePair.of(success, content);
    }


    /**
     * 发送短信，成功返回流水号(只支持一个参数)
     *
     * @param url          请求地址
     * @param username     用户名
     * @param password     密码
     * @param content      短信内容
     * @param phoneNumbers 手机号(多个逗号分隔)
     */
    @SneakyThrows(Exception.class)
    private String send(String url, String username, String password, String content, String phoneNumbers) {

        String path = url +
                "?CorpID=" + username +
                "&Pwd=" + password +
                "&Mobile=" + phoneNumbers +
                "&Content=" + URLEncoder.encode(content, "GBK");

        @Cleanup
        InputStreamReader isr = new InputStreamReader(new URL(path).openStream(), StandardCharsets.UTF_8);

        @Cleanup
        BufferedReader br = new BufferedReader(isr);

        return br.readLine();
    }

}

