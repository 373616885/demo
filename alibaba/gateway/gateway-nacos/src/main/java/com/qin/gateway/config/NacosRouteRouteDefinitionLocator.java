package com.qin.gateway.config;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import reactor.core.publisher.Flux;

import java.util.concurrent.Executor;

/**
 * @author qinjp
 */
@Slf4j
@Component
@AllArgsConstructor
public class NacosRouteRouteDefinitionLocator implements RouteDefinitionLocator {

    private final ConfigService configService;

    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    @SneakyThrows
    public Flux<RouteDefinition> getRouteDefinitions() {
        String content = configService.getConfigAndSignListener(GataWayConfig.DATA_ID,
                GataWayConfig.GROUP,
                GataWayConfig.TIME_OUT_MS,
                listener());
        log.warn(content);
        Yaml yaml = new Yaml();
        RouteDefinitionModel routeDefinitionModel = yaml.loadAs(content, RouteDefinitionModel.class);
        return Flux.fromIterable(routeDefinitionModel.getRoutes());
    }

    private Listener listener() {
        return new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }
            @Override
            public void receiveConfigInfo(String configInfo) {
                log.warn("configInfo : " + configInfo);
                applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            }
        };
    }


}
