package com.gradle.sample.closure;

import java.util.function.Consumer;

/**
 * @author qinjp
 * @date 2019-05-20
 **/
public class MyConsumer<String> implements Consumer<String> {
    @Override
    public void accept(String s) {
        System.out.println(s);
    }
}
