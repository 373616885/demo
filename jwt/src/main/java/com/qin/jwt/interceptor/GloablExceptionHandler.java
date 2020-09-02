package com.qin.jwt.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author jinbin
 * @date 2018-07-08 22:37
 */
@ControllerAdvice
public class GloablExceptionHandler {

    @SneakyThrows
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "服务器出错";
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> map= ImmutableMap.of("message", msg);
        return mapper.writeValueAsString(map);
    }
}
