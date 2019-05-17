package com.qin.servlet.embedded.config;

import com.qin.servlet.embedded.filter.MyFilter;
import com.qin.servlet.embedded.listenter.MyListener;
import com.qin.servlet.embedded.servlet.MyServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ServerConfig {

    @Bean
    public ServletRegistrationBean myServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new MyServlet(), "/my/servlet");
        return bean;
    }


    @Bean
    public FilterRegistrationBean myFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setEnabled(true);
        registration.setFilter(new MyFilter());
        registration.setUrlPatterns(Arrays.asList("/*"));
        return registration;
    }


    @Bean
    public ServletListenerRegistrationBean myListener() {
        ServletListenerRegistrationBean registrationBean = new ServletListenerRegistrationBean();
        registrationBean.setListener(new MyListener());
        return registrationBean;
    }


}
