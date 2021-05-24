package com.qin.nacos.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * config_tag_relation
 * </p>
 *
 * @author qinjp
 * @date 2020-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConfigTagsRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * tag_name
     */
    private String tagName;

    /**
     * tag_type
     */
    private String tagType;

    /**
     * data_id
     */
    private String dataId;

    /**
     * group_id
     */
    private String groupId;

    /**
     * tenant_id
     */
    private String tenantId;

    @TableId(value = "nid", type = IdType.AUTO)
    private Long nid;


}
