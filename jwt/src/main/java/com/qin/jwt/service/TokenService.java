package com.qin.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.qin.jwt.entity.User;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @author jinbin
 * @date 2018-07-08 21:04
 */
@Service("TokenService")
public class TokenService {
    public String getToken(User user) {
        //过期时间
        Date date = new Date(System.currentTimeMillis() + 5 * 1000);
        String token = "";
        token = JWT.create().withAudience(user.getId())// 将 user id 保存到 token 里面
                .withExpiresAt(date)
                .withClaim("username", user.getUsername())
                .withClaim("password", user.getPassword())
                .sign(Algorithm.HMAC256(user.getPassword()));// 以 password 作为 token 的密钥
        return token;
    }
}
