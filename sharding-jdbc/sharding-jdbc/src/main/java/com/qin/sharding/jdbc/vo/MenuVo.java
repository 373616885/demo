package com.qin.sharding.jdbc.vo;

import lombok.Data;

import java.util.List;

/**
 * @author qinjp
 * @date 2020/10/20
 */
@Data
public class MenuVo {

    /**
     * ID
     */
    private Integer id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * url
     */
    private String url;

    /**
     * 排序
     */
    private Integer seq;

    /**
         * （0：菜单，1：按钮）
     */
    private String type;

    /**
     * 父类ID
     */
    private Integer parentId;

    /**
     * 子菜单列表
     */
    private List<MenuVo> childMenu;


}
