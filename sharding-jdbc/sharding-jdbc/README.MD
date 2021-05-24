# ShardingSphere-JDBC的一个坑（SQLFeatureNotSupportedException: getObject with type）



ShardingSphere-JDBC 不支持操作

```java
AbstractUnsupportedOperationResultSet
```

## 解决方案：

自己写处理类：处理LocalDateTime和LocalDate

```java
package com.qin.sharding.jdbc.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author qinjp
 * @date 2020/12/3
 */
@Component
//定义转换器支持的JAVA类型
@MappedTypes(LocalDateTime.class)
//定义转换器支持的数据库类型
@MappedJdbcTypes(value = JdbcType.TIMESTAMP, includeNullJdbcType = true)
public class SharddingJdbcLocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            ps.setString(i, dateTimeFormatter.format(parameter));
        }
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String target = rs.getString(columnName);
        if (StringUtils.hasText(target)) {
            return LocalDateTime.parse(target, dateTimeFormatter);
        }
        return null;
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String target = rs.getString(columnIndex);
        if (StringUtils.hasText(target)) {
            return LocalDateTime.parse(target, dateTimeFormatter);
        }
        return null;
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String target = cs.getString(columnIndex);
        if (StringUtils.hasText(target)) {
            return LocalDateTime.parse(target, dateTimeFormatter);
        }
        return null;
    }
}

```



```java
package com.qin.sharding.jdbc.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author qinjp
 * @date 2020/12/3
 */
@Component
//定义转换器支持的JAVA类型
@MappedTypes(LocalDate.class)
//定义转换器支持的数据库类型
@MappedJdbcTypes(value = JdbcType.DATE, includeNullJdbcType = true)
public class SharddingJdbcLocalDateTypeHandler extends BaseTypeHandler<LocalDate> {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            ps.setString(i, dateFormatter.format(parameter));
        }
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String target = rs.getString(columnName);
        if (StringUtils.hasText(target)) {
            return LocalDate.parse(target, dateFormatter);
        }
        return null;
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String target = rs.getString(columnIndex);
        if (StringUtils.hasText(target)) {
            return LocalDate.parse(target, dateFormatter);
        }
        return null;
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String target = cs.getString(columnIndex);
        if (StringUtils.hasText(target)) {
            return LocalDate.parse(target, dateFormatter);
        }
        return null;
    }
}

```



# sharding-jdbc什么情况select会走主库

- select前增加`HintManager.getInstance().setMasterRouteOnly();`类似于pagehelper只对下一条查询语句生效。
- 存在事务的情况下，事务内的更新语句之后的select会走master，记住只有之后的，之前的select还是会走从库。



