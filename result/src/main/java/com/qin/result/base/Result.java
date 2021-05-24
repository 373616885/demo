package com.qin.result.base;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author qinjp
 * @date 2019-05-30
 **/
@Data
@Builder
@Accessors(chain = true)
public class Result {

    private Integer code;

    private String message;

    private Object data;

    public static Result success() {
        return Result.builder().build().setResultCode(ResultCode.SUCCESS);
    }

    public static Result success(Object data) {
        return Result.success().setData(data);
    }

    public static Result fail() {
        return Result.builder().build().setResultCode(ResultCode.FAIL);
    }

    public static Result fail(Object data) {
        return Result.fail().setData(data);
    }

    private Result setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        return this;
    }
}
