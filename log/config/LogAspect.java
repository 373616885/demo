package com.zzsim.gz.airport.web.log.config;

import com.zzsim.gz.airport.web.base.Constant;
import com.zzsim.gz.airport.web.log.domain.LogEvent;
import com.zzsim.gz.airport.web.util.IpUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 操作日aop配置--拦截所有 @PostMapping 和 @GetMapping
 *
 * @author qinjp
 * @date 2020/10/12
 */
@Slf4j
@Aspect
@Component
@AllArgsConstructor
@SuppressWarnings("all")
public class LogAspect {

    private final AsyncLogDisruptor asyncLogDisruptor;

    private final HttpServletRequest request;

    /**
     * 定义切入点
     */
//    @Pointcut(" (@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
//            " @annotation(org.springframework.web.bind.annotation.GetMapping))   &&  " +
//            " !execution(public * com.zzsim.gz.airport.web.web.LogController.*(..)) ")
//    public void pointcut() {
//    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void two() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void three() {
    }

    @Pointcut("execution(public * com.zzsim.gz.airport.web.web.LogController.*(..))")
    public void four() {
    }

    @Pointcut("(two() || three()) && !four()")
    public void five() {
    }


    @SneakyThrows
    @Around("five()")
    public Object around(ProceedingJoinPoint p) {
        // 执行方法
        Object result = p.proceed();

        // 构建日志对象
        LogEvent logEvent = new LogEvent();
        logEvent.setToken(request.getHeader(Constant.TOKEN));
        logEvent.setHost(IpUtils.getRemoteIp());
        logEvent.setUrl(request.getRequestURI());
        logEvent.setParam(p.getArgs());
        logEvent.setResult(result);
        // 异步队列处理
        asyncLogDisruptor.publish(logEvent);

        return result;
    }

    @AfterThrowing(pointcut = "five()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        // 构建错误日志对象
        LogEvent logEvent = new LogEvent();
        logEvent.setToken(request.getHeader(Constant.TOKEN));
        logEvent.setHost(IpUtils.getRemoteIp());
        logEvent.setUrl(request.getRequestURI());
        logEvent.setParam(joinPoint.getArgs());
        logEvent.setResult(e.getMessage());
        asyncLogDisruptor.publish(logEvent);
    }


}
