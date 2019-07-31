package com.qin.annotation.bean;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("employee")
@Data
public class Employee {

    private Integer id;
    private String lastName;
    private Integer gender;
    private String email;
    private Integer dId;

}
