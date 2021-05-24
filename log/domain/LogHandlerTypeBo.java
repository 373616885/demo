package com.zzsim.gz.airport.web.log.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.BiFunction;

/**
 * @author qinjp
 * @date 2020/10/14
 */
@Data
@AllArgsConstructor
public class LogHandlerTypeBo {

    /**
     * 接口前缀
     */
    private String url;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 操作内容处理方法
     * 第一个参数 url
     * 第二个参数 方法参数
     * 第三个参数 操作内容
     */
    private BiFunction<String, Object[], String> handler;


}
