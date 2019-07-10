package com.qin.jdbc.config;

import com.qin.jdbc.service.ArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ArgsConstructor.class})
public class Config {
}
