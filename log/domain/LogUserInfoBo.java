package com.zzsim.gz.airport.web.log.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qinjp
 * @date 2020/10/14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogUserInfoBo {

    /**
     * 操作人ID
     */
    private Integer accountId;

    /**
     * 账号
     */
    private String username;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 手机号
     */
    private String mobile;

}
