package com.qin.jasypt.web;

import com.qin.jasypt.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("role")
    public List<Role> role() {

        List<Role> roles = jdbcTemplate.query("select * from t_role", (rs, rowNum) -> {
            Role role = new Role();
            role.setId(rs.getInt(1));
            role.setRoleCode(rs.getString(2));
            role.setRoleName(rs.getString(3));
            role.setStatus(rs.getBoolean(4));
            return role;
        });

        return roles;
    }
}
