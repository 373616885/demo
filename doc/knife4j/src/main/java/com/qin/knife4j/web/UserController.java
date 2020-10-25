package com.qin.knife4j.web;

import com.qin.knife4j.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags="用户管理",value = "这个用户管理controller" )
@RestController
public class UserController {


    @ApiOperation(value = "保存用户信息" ,notes = "这个保存用户信息的注释")
    @GetMapping("/saveSysUser")
    public String saveSysUser(@RequestBody SysUser sysUser) {
        //保存用户信息
        return "用户保存成功！";
    }

    /**
     * 获取用户信息
     * @return ResultT<com.missye.swagger.SysUser>
     * @author missye
     * @since 2020/7/21
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/getSysUser")
    public SysUser getSysUser() {
        return new SysUser();
    }

    /**
     * 获取所有用户信息
     * @param
     * @return ResultT<java.util.List < com.missye.swagger.SysUser>>
     * @author missye
     * @since 2020/7/21
     */
    @ApiOperation("获取所有用户信息")
    @GetMapping("/getAllSysUser")
    public List<SysUser> getAllSysUser() {
        return new ArrayList<>();
    }


}
