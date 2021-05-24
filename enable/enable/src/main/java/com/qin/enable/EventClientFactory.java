package com.qin.enable;

public class EventClientFactory {

    public static Client createClient(Config config) {
        return Client.builder()
                .name(config.getName())
                .driverClassName(config.getDriverClassName())
                .url(config.getUrl())
                .username(config.getUsername())
                .password(config.getPassword())
                .build();
    }

}
