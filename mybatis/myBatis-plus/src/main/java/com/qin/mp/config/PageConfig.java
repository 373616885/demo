package com.qin.mp.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Configuration
public class PageConfig {

    public static final ThreadLocal<String> mytable = new ThreadLocal<>();

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        Map<String, TableNameHandler> tableNameHandlerMap = new HashMap<>();
        TableNameHandler one = (String sql, String tableName) -> {
            // metaObject 可以获取传入参数，这里实现你自己的动态规则
            String year = "_2018";
            int random = new Random().nextInt(10);
            if (random % 2 == 1) {
                year = "_2019";
            }
            return tableName + year;
        };

        TableNameHandler user = (sql, tableName) -> mytable.get() == null ? tableName : mytable.get();

        tableNameHandlerMap.put("one", one);
        tableNameHandlerMap.put("t_user", user);

        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandlerMap(tableNameHandlerMap);

        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);

		interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(1);
            }

            // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
            @Override
            public boolean ignoreTable(String tableName) {
                return !"user".equalsIgnoreCase(tableName);
            }
        }));


        return interceptor;
    }

    /**
     * 字段自动化填充
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MyBatisAutoFillHandler();
    }


}

