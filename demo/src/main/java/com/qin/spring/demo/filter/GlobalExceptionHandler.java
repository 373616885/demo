package com.qin.spring.demo.filter;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 所有异常报错
     */
    @ExceptionHandler(value = Exception.class)
    public String allExceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
        exception.printStackTrace();
        logger.error("异常：" + exception.getLocalizedMessage());
        logger.error("异常：" + exception.getCause());
        logger.error("异常：" + exception.getSuppressed());
        logger.error("异常：" + exception.getMessage());
        logger.error("异常：" + exception.getStackTrace());
        return "服务器异常，请联系管理员！";
    }
}
