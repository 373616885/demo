package com.qin.dynamic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qin.dynamic.enums.StatusEnum;
import lombok.Data;

/**
 * 菜单表
 *
 * @author qinjp
 * @date 2020-10-20
 */
@Data
@TableName("t_menu")
public class Menu {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * url
     */
    @TableField(value = "url")
    private String url;

    /**
     * 排序
     */
    @TableField(value = "seq")
    private Integer seq;

    /**
     * （0：菜单，1：按钮）
     */
    @TableField(value = "type")
    private String type;

    /**
     * 父类ID
     */
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 是否启用状态（0：停用，1：启用）
     */
    @TableField(value = "status")
    private StatusEnum status;


}
