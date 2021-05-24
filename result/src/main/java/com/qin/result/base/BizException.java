package com.qin.result.base;

/**
 * 自定义异常
 * @author qinjp
 * @date 2019-05-30
 **/
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 4738244051731004899L;

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

}