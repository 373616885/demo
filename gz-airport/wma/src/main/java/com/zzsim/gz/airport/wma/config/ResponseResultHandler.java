package com.zzsim.gz.airport.wma.config;

import com.zzsim.gz.airport.common.base.OptResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * Controller 统一返回值
 *
 * @author qinjp
 * @date 2019-05-30
 **/
@Slf4j
@RestControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    /**
     * 对Controller直接返回的字符串不做处理
     */
    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return Objects.requireNonNull(returnType.getMethod()).getReturnType() != String.class;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        return body instanceof OptResult ? body : OptResult.success(body);
    }
}
