package com.qin.nacos.mybatis.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author qinjp
 * @date 2020-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Permissions implements Serializable {

    private static final long serialVersionUID = 1L;

    private String role;

    private String resource;

    private String action;


}
