package com.zzsim.gz.airport.wma.config;

import com.zzsim.gz.airport.common.base.OptResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @author qinjp
 * @date 2019-05-30
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * jsr-303异常报错
     * 对象参数接收请求体，即 @RequestBody：
     * MethodArgumentNotValidException
     * 请求参数绑定到对象参数上：
     * BindException
     * 普通参数：
     * ConstraintViolationException
     * 必填参数缺失：
     * ServletRequestBindingException
     */
    @ExceptionHandler({ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            ServletRequestBindingException.class,
            BindException.class})
    public OptResult bindExceptionHandler(Exception e) {

        if (e instanceof MethodArgumentNotValidException) {
            /**
             *  1. @RequestBody 绑定参数错误
             */
            MethodArgumentNotValidException t = (MethodArgumentNotValidException) e;
            String msg = t.getBindingResult()
                    .getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(","));
            return OptResult.fail(msg);
        }

        if (e instanceof BindException) {
            /**
             * 请求参数绑定错误
             */
            BindException t = (BindException) e;
            String msg = t.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(","));
            return OptResult.fail(msg);
        }

        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException t = (ConstraintViolationException) e;
            String msg = t.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(","));
            return OptResult.fail(msg);
        }

        if (e instanceof MissingServletRequestParameterException) {
            /**
             * required = true 错误
             */
            MissingServletRequestParameterException t = (MissingServletRequestParameterException) e;
            String msg = t.getParameterName() + " is null";
            return OptResult.fail(msg);
        }

        if (e instanceof MissingPathVariableException) {
            MissingPathVariableException t = (MissingPathVariableException) e;
            String msg = t.getVariableName() + " is null";
            return OptResult.fail(msg);
        }

        String msg = "param is empty";
        return OptResult.fail(msg);


    }


    /**
     * 所有异常报错
     */
    @ExceptionHandler(value = Exception.class)
    public OptResult allExceptionHandler(HttpServletRequest request, Exception e) {
        log.error("异常堆栈:", e);
        log.error("异常接口: {}", request.getRequestURL().toString());
        log.error("异常信息: {}", e.getMessage());
        return OptResult.fail(e.getMessage());
    }

}
