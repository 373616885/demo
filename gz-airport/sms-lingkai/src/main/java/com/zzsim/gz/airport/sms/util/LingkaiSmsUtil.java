package com.zzsim.gz.airport.sms.util;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 凌凯短信工具类
 *
 * @author qinjp
 * @date 2020/9/10
 */
@Slf4j
public class LingkaiSmsUtil {

    private LingkaiSmsUtil() {
    }

    /**
     * <p>发送短信，成功返回流水号(只支持一个参数)</p>
     *
     * @param url           请求地址
     * @param username      用户名
     * @param password      密码
     * @param templateCode  短信模板
     * @param templateParam 短信模板参数
     * @param phoneNumbers  手机号(多个逗号分隔)
     * @param captcha       短信验证码
     */
    public static String send(String url, String username, String password, String templateCode, String templateParam, String phoneNumbers, String captcha) throws IOException {
        templateCode = templateCode.replaceAll(templateParam, captcha);
        String content = URLEncoder.encode(templateCode, "GBK");
        String path = url +
                "?CorpID=" + username +
                "&Pwd=" + password +
                "&Mobile=" + phoneNumbers +
                "&Content=" + content;

        @Cleanup
        InputStreamReader isr = new InputStreamReader(new URL(path).openStream(), StandardCharsets.UTF_8);

        @Cleanup
        BufferedReader br = new BufferedReader(isr);

        return br.readLine();
    }
}
