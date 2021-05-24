package com.qin.demo;


public final class BitOperatorUtil {

    /**
     * IM(1, "IM消息") = 1
     * 0000 0000 0000 0001
     */
    public static final long IM = 0x00000001;

    /**
     * SYSTEM = 2
     * 0000 0000 0000 0010
     */
    public static final long SYSTEM = 0x00000002;

    /**
     * MAIL = 4
     * 0000 0000 0000 0100
     */
    public static final long MAIL = 0x00000004;

    /**
     * SMS = 8
     * 0000 0000 0000 1000
     */
    public static final long SMS = 0x00000008;


    /**
     * 判断
     * @param mod 用户当前值
     * @param value  需要判断值
     * @return 是否存在
     */
    public static boolean hasMark(long mod, long value) {
        return (mod & value) == value;
    }

    /**
     * 增加
     * @param mod 已有值
     * @param value  需要添加值
     * @return 新的状态值
     */
    public static long addMark(long mod, long value) {
        if (hasMark(mod, value)) {
            return mod;
        }
        return (mod | value);
    }

    /**
     * 删除
     * @param mod 已有值
     * @param value  需要删除值
     * @return 新值
     */
    public static long removeMark(long mod, long value) {
        if (!hasMark(mod, value)) {
            return mod;
        }
        return mod ^ value;
    }

    public static void main(String[] args) {
        // 5 00000101
        System.out.println(hasMark(5,MAIL));
        System.out.println(removeMark(15,MAIL));

        //整型转二进制
        int a = -1;
        int b = 1;
        System.out.println(Integer.toBinaryString(a));
        System.out.println(Integer.toBinaryString(b));
        System.out.println(Integer.toBinaryString(a+b));
        //二进制转int，二进制用0b开头
        int bn = 0b00000010011111111111111111111111;
        System.out.println(bn);
    }

    /**
     * | 位       | 值   | 说明                         |
     * | -------- | ---- | ---------------------------- |
     * | 00000001 | 1    | 支持IM                       |
     * | 00000010 | 2    | 支持系统消息                 |
     * | 00000011 | 3    | 支持IM、系统消息             |
     * | 00000100 | 4    | 支持邮箱                     |
     * | 00000101 | 5    | 支持邮箱、IM                 |
     * | 00000110 | 6    | 支持邮箱、系统消息           |
     * | 00000111 | 7    | 支持邮箱、IM、系统消息       |
     * | 00001000 | 8    | 支持短信                     |
     * | ...      |      |                              |
     * | 00001111 | 15   | 支持邮箱、IM、系统消息、短信 |
     */
}
