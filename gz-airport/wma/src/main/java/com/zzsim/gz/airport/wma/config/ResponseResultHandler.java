package com.zzsim.gz.airport.wma.config;

import com.zzsim.gz.airport.common.base.OptResult;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 统一返回值
 *
 * @author qinjp
 * @date 2019-05-30
 **/
@Slf4j
@RestControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Objects.requireNonNull(returnType.getMethod()).getReturnType() != String.class;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return body instanceof OptResult ? body : OptResult.success(body);
    }
}
