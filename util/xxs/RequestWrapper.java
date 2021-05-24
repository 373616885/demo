package com.zzsim.gz.airport.wma.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static cn.hutool.http.HtmlUtil.filter;

/**
 * @author qinjp
 * @date 2020/9/21
 */
@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {

    /**
     * 拓展requestbody无限获取(HttpServletRequestWrapper只能获取一次)
     */
    private final byte[] body;

    RequestWrapper(HttpServletRequest request) {
        super(request);
        byte[] bodyBate = new byte[0];
        try {
            bodyBate = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            log.error("获取requestBody失败: {}", e.getMessage());
        }
        this.body = bodyBate;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        if (ObjectUtils.isEmpty(body)) {
            return null;
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }
            @Override
            public boolean isReady() {
                return false;
            }
            @Override
            @SuppressWarnings("EmptyMethod")
            public void setReadListener(ReadListener readListener) {
            }
            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }


    /**
     * 对数组参数进行特殊字符过滤
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return values;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = clearXss(values[i]);
        }
        return encodedValues;
    }

    /**
     * 对参数中特殊字符进行过滤
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return value == null ? null : clearXss(value);
    }

    /**
     * Get方法参数的获取时过滤--基本没用到
     */
    @Override
    public String getQueryString() {
        String value = super.getQueryString();
        return value == null ? null : clearXss(value);
    }

    /**
     * 获取attribute,特殊字符过滤
     */
    @Override
    public Object getAttribute(String name) {
        Object value = super.getAttribute(name);
        return value instanceof String ? clearXss((String) value) : value;
    }

    /**
     * 对请求头部进行特殊字符过滤
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return value == null ? null : clearXss(value);
    }




    private String clearXss(String value) {
        return filter(value);
    }


}
