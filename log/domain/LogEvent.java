package com.zzsim.gz.airport.web.log.domain;

import lombok.Data;

/**
 * @author qinjp
 * @date 2020/10/13
 */
@Data
public class LogEvent {

    /**
     * 闭环序号
     */
    private long equence;

    /**
     * token
     */
    private String token;

    /**
     * ip
     */
    private String host;

    /**
     * 接口
     */
    private String url;

    /**
     * 参数
     */
    private Object[] param;

    /**
     * 结果
     */
    private Object result;

}
