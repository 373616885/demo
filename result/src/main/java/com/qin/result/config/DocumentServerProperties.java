package com.qin.result.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;


/**
 * 如果一个配置类只配置@ConfigurationProperties注解，
 * 而没有使用@Component，
 * 那么在IOC容器中是获取不到properties 配置文件转化的bean
 * 相当于把使用 @ConfigurationProperties 的类进行了一次注入
 */
@Data
@ConfigurationProperties(prefix = "doc")
@Validated
@Valid
public class DocumentServerProperties {


    @NotBlank
    private String remoteAddress;

    @Min(80)
    @Max(65536)
    private Integer port;

    private Integer maxConnections;

    private Boolean preferIpAddress;

    private AuthInfo authInfo;

    private List<String> whitelist;

    private Map<String, String> converter;

    private List<Person> defaultShareUsers;

    @Getter
    @Setter
    @ToString
    public static class AuthInfo {

        private String username;
        private String password;
    }


    @Getter
    @Setter
    public static class Person {
        private String name;
        private String age;
    }
}
