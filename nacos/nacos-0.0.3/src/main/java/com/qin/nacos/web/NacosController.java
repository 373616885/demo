package com.qin.nacos.web;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.qin.nacos.mybatis.client.RolesMapper;
import com.qin.nacos.mybatis.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author qinjp
 * @date 2020/12/29
 */
@RestController
@AllArgsConstructor
@Slf4j
public class NacosController {

    private final RolesMapper rolesMapper;

    @GetMapping("/roles")
    public List<Roles> queryRoles(){
        return new LambdaQueryChainWrapper<>(rolesMapper).list();
    }

}
