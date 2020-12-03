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
