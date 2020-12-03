package com.qin.sharding.jdbc.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qin.sharding.jdbc.dao.MenuMapper;
import com.qin.sharding.jdbc.entity.Menu;
import com.qin.sharding.jdbc.enums.StatusEnum;
import com.qin.sharding.jdbc.vo.MenuVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author qinjp
 * @date 2020/12/2
 */
@Slf4j
@RestController
@AllArgsConstructor
public class MenuController {

    private final MenuMapper menuMapper;

    @GetMapping(value = "menu")
    public List<MenuVo> menu() {
        LambdaQueryWrapper<Menu> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(Menu::getStatus, StatusEnum.ENABLE);
        List<Menu> menus = menuMapper.selectList(lambdaQuery);
        List<MenuVo> vos = menus.stream().map(menu -> {
            MenuVo vo = new MenuVo();
            vo.setId(menu.getId());
            vo.setName(menu.getName());
            vo.setUrl(menu.getUrl());
            vo.setSeq(menu.getSeq());
            vo.setType(menu.getType());
            vo.setParentId(menu.getParentId());
            return vo;
        }).collect(Collectors.toList());

        return assemble(vos);
    }

    /**
     * 菜单拼装
     */
    private List<MenuVo> assemble(List<MenuVo> vos) {
        List<MenuVo> roots = new ArrayList<>();
        for (MenuVo vo : vos) {
            if (Objects.equals(vo.getParentId(), -1)) {
                roots.add(vo);
                // 递归查找
                assembleChild(vo, vos);
            }
        }
        // 根节点排序
        roots.sort(Comparator.comparing(MenuVo::getSeq));
        return roots;
    }

    /**
     * 递归查找当前 parent 的子集
     */
    private void assembleChild(MenuVo parent, List<MenuVo> vos) {
        List<MenuVo> children = new ArrayList<>();
        for (MenuVo vo : vos) {
            // 当前节点的父类ID == 父类节点的ID, 当前节点就属于当前父类的子集
            if (Objects.equals(vo.getParentId(), parent.getId())) {
                children.add(vo);
                // 递归查找
                assembleChild(vo, vos);
            }
        }
        // 当前 parent 的子集
        parent.setChildMenu(children);
        // 对子节点进行排序
        children.sort(Comparator.comparing(MenuVo::getSeq));
    }


}
