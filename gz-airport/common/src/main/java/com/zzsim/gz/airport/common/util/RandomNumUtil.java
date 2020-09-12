package com.zzsim.gz.airport.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数工具类
 *
 * @author qinjp
 * @date 2020/9/10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomNumUtil {

    private static final int LEN_PERSONAL_SIGNUP = 6;

    private static final String FORMAT_PERSONAL_SIGNUP = "%0" + LEN_PERSONAL_SIGNUP + "d";

    /**
     * 随机n位数字
     */
    public static String getRandomNum(Integer length) {
        int nextInt = ThreadLocalRandom.current().nextInt();
        return String.format(FORMAT_PERSONAL_SIGNUP, Math.abs(nextInt)).substring(0, length);
    }
}
