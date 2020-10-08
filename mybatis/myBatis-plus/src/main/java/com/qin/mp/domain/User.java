package com.qin.mp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user")
public class User implements Serializable {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField(value = "name" ,updateStrategy = FieldStrategy.NOT_EMPTY,insertStrategy = FieldStrategy.NOT_EMPTY)
    private String name;

    @TableField("age")
    private Integer age;

    @TableField("email")
    private String email;

    @TableField("manager_id")
    private Long managerId;

    @TableField("create_time")
    private LocalDateTime createTime;

    @JsonIgnore
    @TableField("password")
    private String password;

    /**
     * 备注 --非数据库表字段
     */
    @TableField(exist = false)
    private String remark;
}
