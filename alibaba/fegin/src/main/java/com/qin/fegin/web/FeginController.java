package com.qin.fegin.web;

import com.qin.fegin.entity.Roles;
import com.qin.fegin.fegin.ProviderClient;
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
public class FeginController {

    private final ProviderClient providerClient;

    @GetMapping("/roles")
    public List<Roles> queryRoles() {
        var roles = new Roles();
        roles.setRole("qin");
        roles.setUsername("qinjp");
        var result = providerClient.queryRoles(roles);
        log.info(result.toString());
        return result;
    }

}
