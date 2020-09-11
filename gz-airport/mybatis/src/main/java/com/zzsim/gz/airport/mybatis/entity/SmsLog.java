package com.zzsim.gz.airport.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 短信记录
 * </p>
 *
 * @author qinjp
 * @since 2020-09-11
 */
@Data
@TableName("sms_log")
public class SmsLog {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 业务类型
     */
    @TableField("biz_type")
    private String bizType;

    /**
     * 主机
     */
    @TableField(value = "host" , fill = FieldFill.INSERT_UPDATE)
    private String host;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create" , fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 创建人
     */
    @TableField(value = "creater" , fill = FieldFill.INSERT)
    private String creater;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified" , fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 更新人
     */
    @TableField(value = "modifier" , fill = FieldFill.INSERT_UPDATE)
    private String modifier;


}
