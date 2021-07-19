package com.qin.gateway.config;

import com.alibaba.nacos.api.config.ConfigService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author qinjp
 */
@Component
@AllArgsConstructor
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

    private final ConfigService configService;


    @Override
    @SneakyThrows
    public Flux<RouteDefinition> getRouteDefinitions() {
        String content = configService.getConfig(GataWayConfig.DATA_ID,
                GataWayConfig.GROUP,
                GataWayConfig.TIME_OUT_MS);
        Yaml yaml = new Yaml();
        RouteDefinitionModel routeDefinitionModel = yaml.loadAs(content,RouteDefinitionModel.class);
        return Flux.fromIterable(routeDefinitionModel.getRoutes());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
