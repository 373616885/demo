package com.gradle.sample.closure;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author qinjp
 * @date 2019-05-20
 **/
public class TestConsumer {

    public void c () {
        List<String> l = Arrays.asList("a","b","c");
        //l.forEach(MyConsumer::accept);
    }
    // 用::提取的函数，最主要的区别在于静态与非静态方法，非静态方法比静态方法多一个参数，就是被调用的实例
    public static void main(String[] args) {
        List<String> l = Arrays.asList("a","b","c");
        l.forEach(new MyConsumer<>());
        l.forEach(s -> System.out.println(s));

        Consumer<String> printStrConsumer = DoubleColon::printStr;
        printStrConsumer.accept("printStrConsumer");

        // 非静态方法的第一个参数为被调用的对象，后面是入参。静态方法因为jvm已有对象，直接接收入参。
        Consumer<DoubleColon> toUpperConsumer = DoubleColon::toUpper;
        toUpperConsumer.accept(new DoubleColon());

        BiConsumer<DoubleColon,String> toLowerConsumer = DoubleColon::toLower;
        toLowerConsumer.accept(new DoubleColon(),"toLowerConsumer");

        BiFunction<DoubleColon,String,Integer> toIntFunction = DoubleColon::toInt;
        int i = toIntFunction.apply(new DoubleColon(),"toInt");

        // 非静态方法的第一个参数为被调用的对象，后面是入参。静态方法因为jvm已有对象，直接接收入参。
        Convert<String,Integer> toIntc= new DoubleColon()::toInt;
        int ic = toIntFunction.apply(new DoubleColon(),"toInt");

        // 非静态方法的第一个参数为被调用的对象，后面是入参。静态方法因为jvm已有对象，直接接收入参。
        ConsumberInvokevirtual<DoubleColon> consumberInvokevirtual = DoubleColon::toUpper;
        toUpperConsumer.accept(new DoubleColon());
    }
}
