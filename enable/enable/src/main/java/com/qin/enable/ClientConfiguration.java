package com.qin.enable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE + 40)
@Conditional(IsServlet.class)
public class ClientConfiguration {

    @Bean
    public Client eventClient(Config config) {
        return EventClientFactory.createClient(config);
    }
}
