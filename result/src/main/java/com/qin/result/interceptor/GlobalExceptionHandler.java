package com.qin.result.interceptor;

import com.qin.result.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author qinjp
 * @date 2019-05-30
 **/
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * jsr-303异常报错
     */
    @ExceptionHandler(value = BindException.class)
    public Result bindExceptionHandler(Exception e) {
        BindException ex = (BindException) e;
        List<ObjectError> errors = ex.getAllErrors();
        StringBuilder sb = new StringBuilder();
        for (ObjectError error : errors) {
            sb.append(error.getDefaultMessage());
            sb.append(" ");
        }
        return Result.fail(sb.toString());
    }


    /**
     * 所有异常报错
     */
    @ExceptionHandler(value = Exception.class)
    public Result allExceptionHandler(HttpServletRequest request, Exception e){
        log.error("异常堆栈:", e);
        log.error("异常接口: {}", request.getRequestURL().toString());
        log.error("异常信息: {}", e.getMessage());
        return Result.fail(e.getMessage());
    }

}
