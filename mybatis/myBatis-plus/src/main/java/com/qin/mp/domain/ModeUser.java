package com.qin.mp.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 必须存在对应的原始mapper并继承baseMapper并且可以使用的前提下
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
public class ModeUser extends Model<ModeUser> {

    //@TableId(value = "user_id", type = IdType.ASSIGN_ID)
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField("name")
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
