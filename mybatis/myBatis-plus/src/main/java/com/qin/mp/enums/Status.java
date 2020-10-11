package com.qin.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author qinjp
 * @date 2020/9/14
 */
public enum Status {
    /**
     * 1 正常
     * 2 删除
     */
    NORMAL(1, "正常"),
    /**
     * 1 正常
     * 2 删除
     */
    DELETE(2, "删除");

    @EnumValue
    @JsonValue //标记响应json值
    private Integer code;

    private String desc;

    Status(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
