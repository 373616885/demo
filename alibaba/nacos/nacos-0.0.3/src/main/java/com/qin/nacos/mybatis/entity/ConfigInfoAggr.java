package com.qin.nacos.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 增加租户字段
 * </p>
 *
 * @author qinjp
 * @date 2020-12-29
 */
@Data
public class ConfigInfoAggr {

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
     * datum_id
     */
    private String datumId;

    /**
     * 内容
     */
    private String content;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    private String appName;

    /**
     * 租户字段
     */
    private String tenantId;

}
