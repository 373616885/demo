package com.qin.nacos.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * config_info_beta
 * </p>
 *
 * @author qinjp
 * @date 2020-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConfigInfoBeta implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * data_id
     */
    private String dataId;

    /**
     * group_id
     */
    private String groupId;

    /**
     * app_name
     */
    private String appName;

    /**
     * content
     */
    private String content;

    /**
     * betaIps
     */
    private String betaIps;

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

    /**
     * 租户字段
     */
    private String tenantId;


}
