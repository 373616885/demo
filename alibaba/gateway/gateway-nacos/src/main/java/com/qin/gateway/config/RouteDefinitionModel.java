package com.qin.gateway.config;

import lombok.Data;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 * @author qinjp
 */
@Data
public class RouteDefinitionModel {

    private List<RouteDefinition> routes;

}
