package com.zzsim.gz.airport.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * @author qinjp
 * @date 2020/9/11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegexUtils {

    private static final Pattern MOBILE_PHONE_PATTERN = Pattern.compile("^[1]\\d{10}$");

    /**
     *  正则：手机号（简单）, 1字头＋10位数字即可.
     * @param mobile 手机号
     */
    public static boolean validateMobile(String mobile) {
        return MOBILE_PHONE_PATTERN.matcher(mobile).matches();
    }


}
