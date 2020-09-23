package com.zzsim.gz.airport.wma.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author qinjp
 * @date 2020/9/21
 */
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
    }
}
