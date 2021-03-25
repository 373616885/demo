package com.qin.fegin.fegin;

import com.qin.fegin.entity.Roles;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("dev-service")
public interface ProviderClient {

    @PostMapping("/roles")
    List<Roles> queryRoles(@RequestBody Roles roles);
}
