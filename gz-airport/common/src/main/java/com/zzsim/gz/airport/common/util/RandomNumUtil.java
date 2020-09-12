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

    /**
     * %06d 使用String.format 数字前面补0
     */
    private static final String FORMATTER = "%06d";

    /**
     * 随机n位数字
     * 这里注意n要大于零小于六
     * 当随机数位数小于六位时，length > 6  substring 就会出现问题
     * %06d 这个只能填充六位
     */
    public static String getRandomNum(Integer length) {
        int nextInt = ThreadLocalRandom.current().nextInt();
        return String.format(FORMATTER, Math.abs(nextInt)).substring(0, length);
    }
}
