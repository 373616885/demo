package com.zzsim.gz.airport.wma.config;

import cn.hutool.http.HtmlUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.Collections;

/**
 * Xss过滤器--过滤前端返回的参数
 * xxs 过滤器可能是一个隐藏的 bug 开发的时候要注意
 * 容易引起bug尽量不要用
 *
 * @author qinjp
 * @date 2020/9/21
 */
//@Configuration
public class FilterRegistrationConfig {

    @Bean
    public FilterRegistrationBean<XssFilter> filterRegistrationBean() {
        FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
        XssFilter filter = new XssFilter();
        registrationBean.setFilter(filter);
        //拦截所有
        registrationBean.setUrlPatterns(Collections.singletonList("/*"));
        //设置该过滤器的优先级，数字越小，优先级越高
        registrationBean.setOrder(1);
        return registrationBean;
    }

    /**
     * 过滤json类型的
     */
    @Bean
    @Primary
    public ObjectMapper xssObjectMapper(Jackson2ObjectMapperBuilder builder) {
        //解析器
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        //注册xss解析器
        SimpleModule xssModule = new SimpleModule();
        // 出参转义
        xssModule.addDeserializer(String.class, new XssStringJsonDeserializer());
        // 入参转义
        xssModule.addSerializer(new XssStringJsonSerializer());
        objectMapper.registerModule(xssModule);
        //返回
        return objectMapper;
    }

    /**
     * Xxs json 出参 过滤
     */
    static class XssStringJsonSerializer extends JsonSerializer<String> {

        @Override
        public Class<String> handledType() {
            return String.class;
        }

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                String encodedValue = HtmlUtil.filter(value);
                gen.writeString(encodedValue);
            }
        }
    }


    /**
     * Xxs json 入参 过滤
     */
    static class XssStringJsonDeserializer extends JsonDeserializer<String> {
        @Override
        public Class<?> handledType() {
            return String.class;
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String value = p.getValueAsString();
            if (value != null) {
                return HtmlUtil.filter(value);
            }
            return value;
        }
    }

}