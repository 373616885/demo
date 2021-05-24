package com.qin.result.config;

import com.qin.result.model.Third;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DocumentServerProperties.class)
public class PropertiesConfiguration {

    @Bean
    public DocumentServerProperties documentServerProperties(){
        return new DocumentServerProperties();
    }

    @Bean
    @ConfigurationProperties("demo.third")
    public Third thirdComponent(){
        return new Third();
    }
}
