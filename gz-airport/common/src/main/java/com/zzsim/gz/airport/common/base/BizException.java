package com.zzsim.gz.airport.common.base;

/**
 * 自定义异常
 * @author qinjp
 * @date 2020/9/9
 */
public class BizException extends RuntimeException {

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }
}
