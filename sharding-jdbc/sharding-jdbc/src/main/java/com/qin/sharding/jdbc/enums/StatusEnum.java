package com.qin.sharding.jdbc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 菜单状态枚举
 *
 * @author qinjp
 * @date 2020-10-20
 */
public enum StatusEnum {

    /**
     * 0：停用，1：启用
     */
    STOP(0),
    ENABLE(1);

    @EnumValue
    @JsonValue
    private Integer value;

    StatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
