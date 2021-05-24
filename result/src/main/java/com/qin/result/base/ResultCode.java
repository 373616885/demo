package com.qin.result.base;

/**
 * @author qinjp
 * @date 2019-05-30
 **/
public enum ResultCode {
    /** 成功 **/
    SUCCESS(200,"成功"),
    /** 失败 **/
    FAIL(500,"失败");

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
