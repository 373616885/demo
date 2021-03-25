package com.qin.nacos.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 多租户改造
 * </p>
 *
 * @author qinjp
 * @date 2020-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HisConfigInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @TableId(value = "nid", type = IdType.AUTO)
    private Long nid;

    private String dataId;

    private String groupId;

    /**
     * app_name
     */
    private String appName;

    private String content;

    private String md5;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private String srcUser;

    private String srcIp;

    private String opType;

    /**
     * 租户字段
     */
    private String tenantId;


}
