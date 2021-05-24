package com.gradle.sample.closure;

import java.util.function.Function;

/**
 * @Author qinjp
 **/
public class Fun {

    static Function<String,String> func =  String::toUpperCase;

    public static void main(String[] args) {
        System.out.println(func.apply("abc"));
    }
}
