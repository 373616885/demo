package com.qin.result.domain;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author qinjp
 * @date 2019-05-30
 **/
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Cat {

    @NotNull(message = "cat id is null")
    private Integer id;

    @NotEmpty(message = "cat name is empty")
    private String name;

}
