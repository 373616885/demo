package com.qin.nacos.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 租户容量信息表
 * </p>
 *
 * @author qinjp
 * @date 2020-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TenantCapacity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Tenant ID
     */
    private String tenantId;

    /**
     * 配额，0表示使用默认值
     */
    private Integer quota;

    /**
     * 使用量
     */
    private Integer usage;

    /**
     * 单个配置大小上限，单位为字节，0表示使用默认值
     */
    private Integer maxSize;

    /**
     * 聚合子配置最大个数
     */
    private Integer maxAggrCount;

    /**
     * 单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值
     */
    private Integer maxAggrSize;

    /**
     * 最大变更历史数量
     */
    private Integer maxHistoryCount;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;


}
