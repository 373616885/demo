package com.zzsim.gz.airport.web.log.service;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.ImmutableList;
import com.zzsim.gz.airport.web.log.domain.LogHandlerTypeBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author jiepeng.tan
 */
@Slf4j
@Service
public final class HandlerType {

    private final List<LogHandlerTypeBo> handlers = ImmutableList.of(
            new LogHandlerTypeBo("/query", "查询", EMPTY),
            new LogHandlerTypeBo("/delete", "删除", DELETE),
            new LogHandlerTypeBo("/update", "修改", UPDATE),
            new LogHandlerTypeBo("/insert", "新增", INSERT),
            new LogHandlerTypeBo("/account/get/skey", "登录", LOGIN),
            new LogHandlerTypeBo("/account/logout", "退出", EMPTY)
    );


    LogHandlerTypeBo getHandlerType(String url) {
        for (LogHandlerTypeBo handler : handlers) {
            if (StringUtils.startsWith(url, handler.getUrl())) {
                return handler;
            }
        }
        return new LogHandlerTypeBo(url, "未知", EMPTY);
    }

    /**
     * 不处理直接放回 null
     */
    private static final BiFunction<String, Object[], String> EMPTY = (url, param) -> null;

    /**
     * 删除接口获取 url最后一个 ‘/’ 之后的字符
     */
    private static final BiFunction<String, Object[], String> DELETE = (url, param) -> {
        int index = StringUtils.lastIndexOf(url, "/");
        return StringUtils.substring(url, index);
    };

    /**
     * 修改获取参数的id属性
     */
    private static final BiFunction<String, Object[], String> UPDATE = (url, param) -> {
        try {
            Object obj = param[0];
            Object id = ReflectUtil.getFieldValue(obj, "id");
            return Objects.isNull(id) ? null : String.valueOf(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    };

    /**
     * 添加获取第一个参数的第一个属性
     */
    private static final BiFunction<String, Object[], String> INSERT = (url, param) -> {
        try {
            Object obj = param[0];
            Field[] fields = ReflectUtil.getFields(obj.getClass());
            Field field = fields[0];
            return String.valueOf(ReflectUtil.getFieldValue(obj, field));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    };

    /**
     * 添加获取参数的第一个属性
     */
    private static final BiFunction<String, Object[], String> LOGIN = (url, param) -> {
        try {
            Object obj = param[0];
            return String.valueOf(ReflectUtil.getFieldValue(obj, "username"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    };

}
