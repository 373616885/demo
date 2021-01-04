package com.qin.nacos.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * config_info
 * </p>
 *
 * @author qinjp
 * @date 2020-12-29
 */
@Data
@TableName("config_info")
public class ConfigInfo {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * data_id
     */
    @TableField("data_id")
    private String dataId;

    private String groupId;

    /**
     * content
     */
    private String content;

    /**
     * md5
     */
    private String md5;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    /**
     * source user
     */
    private String srcUser;

    /**
     * source ip
     */
    private String srcIp;

    private String appName;

    /**
     * 租户字段
     */
    private String tenantId;

    private String cDesc;

    private String cUse;

    private String effect;

    private String type;

    private String cSchema;


}
