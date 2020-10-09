package com.zzsim.gz.airport.wma.config;

import com.zzsim.gz.airport.wma.base.BizException;
import com.zzsim.gz.airport.wma.base.MsgSource;
import com.zzsim.gz.airport.wma.base.OptResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

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
    public OptResult allExceptionHandler(HttpServletRequest request, Exception e) {
        // 记录日志
        logError(request, e);
        // 默认值
        return OptResult.fail(MSG_FAIL);
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = BizException.class)
    public OptResult bizExceptionHandler(HttpServletRequest request, Exception e) {
        // 记录日志
        logError(request, e);
        // 默认值
        return OptResult.fail(e.getMessage());
    }

    /**
     * 参数异常报错
     */
    @ExceptionHandler({BindException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MissingPathVariableException.class,
            ServletRequestBindingException.class})
    public OptResult bindExceptionHandler(HttpServletRequest request, Exception e) {
        // 记录日志
        logError(request, e);

        if (e instanceof BindException) {
            /*
             * 请求参数绑定错误--多个错误信息只给第一个
             */
            BindException t = (BindException) e;
            String msg = t.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .findFirst()
                    .orElse(MSG_FAIL);
            return OptResult.fail(msg);
        }

        if (e instanceof MethodArgumentNotValidException) {
            /*
             *  @RequestBody 绑定参数错误 --多个错误信息只给第一个
             */
            MethodArgumentNotValidException t = (MethodArgumentNotValidException) e;
            String msg = t.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .findFirst()
                    .orElse(MSG_FAIL);
            return OptResult.fail(msg);
        }

        if (e instanceof ConstraintViolationException) {
            // 多个错误信息只给第一个
            ConstraintViolationException t = (ConstraintViolationException) e;
            String msg = t.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .findFirst()
                    .orElse(MSG_FAIL);
            return OptResult.fail(msg);
        }

        if (e instanceof HttpMessageNotReadableException) {
            // @RequestBody 参数类型不匹配
            return OptResult.fail(MSG_PARAM_ERROR);
        }

        if (e instanceof MissingServletRequestParameterException) {
            /*
             * required = true 错误
             */
            MissingServletRequestParameterException t = (MissingServletRequestParameterException) e;
            return OptResult.fail(MSG_PARAM_MISS, t.getParameterName() + MsgSource.getMsg(MSG_PARAM_MISS));
        }

        if (e instanceof MissingPathVariableException) {
            /*
             * PathVariable 错误：
             */
            MissingPathVariableException t = (MissingPathVariableException) e;
            return OptResult.fail(MSG_PARAM_MISS, t.getVariableName() + MsgSource.getMsg(MSG_PARAM_MISS));
        }

        if (e instanceof ServletRequestBindingException) {
            /*
             * ServletRequestBindingException 是 上面两个的集合
             * 参数绑定错误
             */
            return OptResult.fail(MSG_PARAM_ERROR);
        }
        // 默认值
        return OptResult.fail(MSG_FAIL);
    }

    /**
     * NOT_FOUND 404 异常
     * NoHandlerFoundException
     * get post 方法错误
     * HttpRequestMethodNotSupportedException
     */
    @ExceptionHandler(value = {NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class})
    public OptResult noHandlerFoundException(HttpServletRequest request, Exception e) {
        // 记录日志
        logError(request, e);

        if (e instanceof NoHandlerFoundException) {
            /*
             * 访问 404
             */
            return OptResult.fail(REQUEST_PATH_NOT_FOUND);
        }

        if (e instanceof HttpRequestMethodNotSupportedException) {
            /*
             * get post 方法错误
             */
            return OptResult.fail(REQUEST_METHOD_ERROR);
        }
        // 默认值
        return OptResult.fail(MSG_FAIL);
    }

    private void logError(HttpServletRequest request, Exception e) {
        log.error("异常堆栈:", e);
        log.error("异常接口: {}", request.getRequestURL().toString());
        log.error("异常信息: {}", e.getMessage());
    }

    private static final String MSG_FAIL = "fail";

    private static final String MSG_PARAM_MISS = "param.miss";

    private static final String MSG_PARAM_ERROR = "param.error";

    private static final String REQUEST_PATH_NOT_FOUND = "request.path.not.found";

    private static final String REQUEST_METHOD_ERROR = "request.method.error";

}
