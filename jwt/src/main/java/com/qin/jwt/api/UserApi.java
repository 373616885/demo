package com.qin.jwt.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qin.jwt.annotation.PassToken;
import com.qin.jwt.annotation.UserLoginToken;
import com.qin.jwt.entity.User;
import com.qin.jwt.service.TokenService;
import com.qin.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jinbin
 * @date 2018-07-08 20:45
 */
@RestController
@RequestMapping("api")
public class UserApi {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    //登录
    @PassToken(required = false)
    @PostMapping("/login")
    public Object login( User user){
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> result = new HashMap<>();
        User userForBase=userService.findByUsername(user);
        if(userForBase==null){
            result.put("message","登录失败,用户不存在");
            return result;
        }else {
            if (!userForBase.getPassword().equals(user.getPassword())){
                result.put("message","登录失败,密码错误");
                return result;
            }else {
                String token = tokenService.getToken(userForBase);
                result.put("token", token);
                result.put("user", userForBase);
                return result;
            }
        }
    }
    
    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }
}
