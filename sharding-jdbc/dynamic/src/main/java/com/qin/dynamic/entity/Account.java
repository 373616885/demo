package com.qin.dynamic.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 后台管理员用户
 *
 * @author qinjp
 * @date 2020-09-21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_account")
public class Account {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    @TableField("password")
    private String password;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Integer roleId;

    /**
     * 是否启用状态（0：注销，1：正常，）
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private LocalDateTime gmtCreate;

    /**
     * 创建人
     */
    @TableField(value = "creater", fill = FieldFill.INSERT)
    private String creater;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified", fill = FieldFill.UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 更新人
     */
    @TableField(value = "modifier", fill = FieldFill.INSERT_UPDATE)
    private String modifier;


}
