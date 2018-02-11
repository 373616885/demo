package com.qin.spring.demo;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.qin.spring.demo.filter.SecurityInterceptor;

@Configuration
public class ServletConfig extends WebMvcConfigurerAdapter {
	
	
	@Bean
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
		// 找不到页面拋异常--然后全局异常处理
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		return registration;
	}

	/**
	 * mvc拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/**
		 * 默认拦截所有的路径
		 */
		registry.addInterceptor(new SecurityInterceptor())// .addPathPatterns("/**")
				.excludePathPatterns("static/*");
	}
}
