package com.qin.gataway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qinjiepeng
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 配制一个路由
                // 把 http://网关地址:网关端口/demo/ 下的请求路由到 demo-service 微服务中
                .route("nacos",
                        p -> p.path("/nacos/**")  //url匹配
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://10.11.51.23:8881/")// 将请求路由到指定目标, lb开头是注册中心中的服务, http/https 开头你懂的
                )
                .build();
    }

}
