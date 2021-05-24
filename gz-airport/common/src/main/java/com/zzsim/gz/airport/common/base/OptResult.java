package com.zzsim.gz.airport.common.base;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author qinjp
 * @date 2020/9/9
 */
@Data
@Accessors(chain = true)
public final class OptResult<T> {

    private boolean success;

    private T data;

    public static <T> OptResult<T> success() {
        return new OptResult<T>().setSuccess(true);
    }

    public static <T> OptResult<T> success(T data) {
        return new OptResult<T>().setSuccess(true).setData(data);
    }

    public static <T> OptResult<T> fail() {
        return new OptResult<T>().setSuccess(false);
    }

    public static <T> OptResult<T> fail(T data) {
        return new OptResult<T>().setSuccess(false).setData(data);
    }

}
