package com.qin.nacos.mybatis.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Enabled {

    /**
     * 0：停用，1：启用
     */
    STOP(0),
    ENABLE(1);

    @EnumValue
    @JsonValue
    private Integer value;

    Enabled(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
