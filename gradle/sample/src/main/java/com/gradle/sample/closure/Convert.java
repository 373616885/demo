package com.gradle.sample.closure;

/**
 * @author qinjp
 * @date 2019-05-20
 **/

@FunctionalInterface
public interface Convert<F, T> {
    T convert(F form);
}
