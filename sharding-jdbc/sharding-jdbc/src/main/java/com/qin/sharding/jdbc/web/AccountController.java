package com.qin.sharding.jdbc;

import org.apache.shardingsphere.api.hint.HintManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qin.sharding.jdbc.**.dao")
public class ShardingJdbcApplication {

    public static void main(String[] args) {
		// 强制走主库
        HintManager.getInstance().setMasterRouteOnly();
        SpringApplication.run(ShardingJdbcApplication.class, args);
    }

}
