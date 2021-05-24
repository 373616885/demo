package com.gradle.sample.config;

import com.gradle.sample.domain.Cat;
import com.gradle.sample.domain.Dog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * @author qinjp
 * @date 2019-05-29
 **/
@Configuration
public class Config {

    //@Bean
    //@ConfigurationProperties(prefix = "spring.datasource")
    public DataSource datasource() {
        DataSource dataSource = DataSourceBuilder.create().build();
//        try {
//            System.out.println(dataSource.getConnection().getClientInfo());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return dataSource;
    }

    @Configuration
    @ConditionalOnProperty(name = "qin.conditional.test" ,havingValue = "true", matchIfMissing = false)
    @Import({Dog.class, Cat.class,ConfigTest.StudentConfig.class})
    static class QinConditionalTest {



    }


}
