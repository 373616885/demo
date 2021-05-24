package com.zzsim.gz.airport.wma.config;

import com.zzsim.gz.airport.common.base.MsgSource;
import com.zzsim.gz.airport.common.base.OptResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author qinjp
 * @date 2019-05-30
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 所有异常报错
     */
    @ExceptionHandler(value = Exception.class)
    public OptResult<String> allExceptionHandler(HttpServletRequest request, Exception e) {
        // 记录日志
        logError(request, e);
        // 默认值
        return OptResult.fail(MsgSource.getMsg(e.getMessage()));
    }


    /**
     * jsr-303异常报错
     * 请求参数绑定到对象参数上：
     * BindException
     * 对象参数接收请求体，即 @RequestBody：
     * MethodArgumentNotValidException
     * 普通参数：
     * ConstraintViolationException
     * 必填参数缺失：
     * ServletRequestBindingException
     */
    @ExceptionHandler({ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            ServletRequestBindingException.class,
            BindException.class})
    public OptResult<String> bindExceptionHandler(HttpServletRequest request, Exception e) {
        // 记录日志
        logError(request, e);

        if (e instanceof BindException) {
            /*
             * 请求参数绑定错误
             */
            BindException t = (BindException) e;
            String msg = t.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .map(MsgSource::getMsg)
                    .collect(Collectors.joining(","));
            return OptResult.fail(msg);
        }

        if (e instanceof MethodArgumentNotValidException) {
            /*
             *  @RequestBody 绑定参数错误
             */
            MethodArgumentNotValidException t = (MethodArgumentNotValidException) e;
            String msg = t.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .map(MsgSource::getMsg)
                    .collect(Collectors.joining(","));
            return OptResult.fail(msg);
        }

        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException t = (ConstraintViolationException) e;
            String msg = t.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .map(MsgSource::getMsg)
                    .collect(Collectors.joining(","));
            return OptResult.fail(msg);
        }

        if (e instanceof MissingServletRequestParameterException) {
            /*
             * required = true 错误
             */
            MissingServletRequestParameterException t = (MissingServletRequestParameterException) e;
            return OptResult.fail(t.getParameterName() + MsgSource.getMsg("param.miss"));
        }

        if (e instanceof MissingPathVariableException) {
            MissingPathVariableException t = (MissingPathVariableException) e;
            return OptResult.fail(t.getVariableName() + MsgSource.getMsg("param.miss"));
        }
        // 默认值
        return OptResult.fail(MsgSource.getMsg(e.getMessage()));
    }


    private void logError(HttpServletRequest request, Exception e) {
        log.error("异常堆栈:", e);
        log.error("异常接口: {}", request.getRequestURL().toString());
        log.error("异常信息: {}", e.getMessage());
    }

}
