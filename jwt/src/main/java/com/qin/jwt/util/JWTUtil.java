package com.qin.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.ImmutableMap;
import com.qin.jwt.entity.User;

import java.util.Date;
import java.util.Map;

public class JWTUtil {

    /**
     * 过期时间为一天
     */
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

    /**
     * 发行人
     */
    private static String ISSUER = "admin";

    /**
     * token私钥 --这个秘钥可以用密码
     */
    private static final String TOKEN_SECRET = "8fuvkQRsSVaPrihOdg59vVr0qYTEmxVv";

    /**
     * jwt 头部声明
     */
    private static final Map<String, Object> header = ImmutableMap.of("typ", "JWT", "alg", "HS256");

    /**
     * 私钥及加密算法
     */
    private static final Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

    /**
     * 生成签名,15分钟后过期
     */
    public static String sign(User user) {
        //过期时间
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        //附带username和userID生成签名
        // aud -- id
        return JWT.create()
                .withIssuer(ISSUER)
                .withHeader(header)
                .withAudience(user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("password", user.getPassword())
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 校验 token
     */
    public static boolean verity(String token) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        }  catch (JWTVerificationException e) {
            return false;
        }
    }


    /**
     * 校验 token
     */
    public static User decrypt(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        User user = new User();
        user.setId(jwt.getAudience().get(0));
        user.setUsername(claims.get("username").asString());
        user.setPassword(claims.get("password").asString());
        return user;
    }


    public static void main(String[] args) {
        User user = new User();
        user.setId("373616885");
        user.setUsername("qinjp");
        user.setPassword("123456");
        String token = JWTUtil.sign(user);
        System.out.println(JWTUtil.decrypt(token));
    }
}
