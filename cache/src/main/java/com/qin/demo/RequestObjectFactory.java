package com.qin.demo;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;

public class RequestObjectFactory {

    public static ServletRequest getObject() {
        return currentRequestAttributes().getRequest();
    }

    @Override
    public String toString() {
        return "Current HttpServletRequest";
    }


    /**
     * Return the current RequestAttributes instance as ServletRequestAttributes.
     *
     * @see RequestContextHolder#currentRequestAttributes()
     */
    private static ServletRequestAttributes currentRequestAttributes() {
        RequestAttributes requestAttr = RequestContextHolder.currentRequestAttributes();
        if (!(requestAttr instanceof ServletRequestAttributes)) {
            throw new IllegalStateException("Current request is not a servlet request");
        }
        return (ServletRequestAttributes) requestAttr;
    }


}
