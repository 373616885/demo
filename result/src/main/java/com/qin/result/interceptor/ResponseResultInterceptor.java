package com.qin.result.interceptor;

import com.google.common.collect.Maps;
import com.qin.result.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;

/**
 * @author qinjp
 * @date 2019-05-30
 **/
@Slf4j
public class ResponseResultInterceptor implements HandlerInterceptor {

    private static final String RESPONSE_RESULT = "RESPONSE_RESULT";

    private static final String SEPARATOR = ".";

    /**
     * 缓存
     **/
    private static final Map<String, Annotation> responseResultCache = Maps.newConcurrentMap();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger(request);

        // 非方法处理器不拦截
        if (handler instanceof HandlerMethod) {

            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 处理@ResponseResult这个注解
            handlerResponseResult(handlerMethod);
        }

        return true;
    }

    private void logger(HttpServletRequest request) {
        log.info("ip : {}", request.getRemoteAddr());
        log.info("port : {}", request.getLocalPort());
        log.info("referer : {}", request.getHeader("referer"));
        log.info("url : {}", request.getRequestURL().toString());
        log.info("Content-Type : {}", request.getHeader("Content-Type"));
        // 请求参数
        Enumeration<String> em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = em.nextElement();
            log.info("请求参数: name: {}  value: {}", name, request.getParameter(name));
        }
    }


    private void handlerResponseResult(final HandlerMethod handlerMethod) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        final Class<?> clazz = handlerMethod.getBeanType();

        Annotation clazzResponseResultAnnotation = responseResultCache.get(clazz.getName());
        // 查询缓存里是否存在类的注解
        if (Objects.nonNull(clazzResponseResultAnnotation)) {
            request.setAttribute(RESPONSE_RESULT, clazzResponseResultAnnotation);
            return;
        }

        final Method method = handlerMethod.getMethod();

        Annotation methodResponseResultAnnotation = responseResultCache.get(clazz.getName() + SEPARATOR + method.getName());
        // 查询缓存里是否存在方法的注解
        if (Objects.nonNull(methodResponseResultAnnotation)) {
            request.setAttribute(RESPONSE_RESULT, methodResponseResultAnnotation);
            return;
        }

        // 类上是否有 @ResponseResult 这个注解
        if (clazz.isAnnotationPresent(ResponseResult.class)) {
            clazzResponseResultAnnotation = clazz.getAnnotation(ResponseResult.class);
            request.setAttribute(RESPONSE_RESULT, clazzResponseResultAnnotation);
            responseResultCache.put(clazz.getName(), clazzResponseResultAnnotation);
            return;
        }

        // 方法上是否有 @ResponseResult 这个注解
        if (method.isAnnotationPresent(ResponseResult.class)) {
            methodResponseResultAnnotation = method.getAnnotation(ResponseResult.class);
            request.setAttribute(RESPONSE_RESULT, methodResponseResultAnnotation);
            responseResultCache.put(clazz.getName() + SEPARATOR + method.getName(), methodResponseResultAnnotation);
        }

    }


}
