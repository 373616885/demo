package com.qin.result.web;


import com.qin.result.config.DocumentServerProperties;
import com.qin.result.model.Third;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@RestController
@AllArgsConstructor
public class SampleController {

    private final DocumentServerProperties documentServerProperties;

    private final Third third;

    @RequestMapping("/documentServerProperties")
    public Object getObjectProperties() {
        return documentServerProperties.toString() + " " + third.toString();
    }


    @RequestMapping("/cookie/abc/qin")
    public Object cookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("acctoken", UUID.randomUUID().toString());
        // http://localhost:8081/cookie/abc/qin
        // 只有 /cookie/abc/ 路径下才能获取到这个cookie
//        cookie.setDomain("localhost");
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        cookie.setMaxAge(30);
        response.addCookie(cookie);
        return documentServerProperties.toString() + " " + third.toString();
    }

}
