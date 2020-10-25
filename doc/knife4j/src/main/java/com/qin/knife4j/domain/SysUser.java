package com.qin.knife4j.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类信息
 * @author missye
 * @since 2020/07/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户基本信息")
public class SysUser {

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;
    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickName;
    /**
     * 邮件
     */
    @ApiModelProperty("邮件")
    private String email;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

}
