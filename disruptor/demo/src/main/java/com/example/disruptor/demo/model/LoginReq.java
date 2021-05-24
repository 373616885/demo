package com.example.disruptor.demo.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
* @Description:   登录请求实体类
* @Author:         dengjihai
* @CreateDate:     2019/6/26
*/
@Data
public class LoginReq implements Serializable {

    @NotNull(message = "手机号不能为空")
    private String mobile;

    @NotNull(message = "短信验证码不能为空")
    private String smsCode;

    @NotNull(message = "设备号不能为空")
    private String deviceNo;

    @NotNull(message = "手机型号不能为空")
    private String pmodel;

}
