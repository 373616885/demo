package com.qin.result.model;

import com.qin.result.domain.Cat;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author qinjp
 * @date 2019-05-30
 **/
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Dog {

    @NotNull(message = "dog id is null")
    private Integer id;

    @NotEmpty(message = "dog name is empty")
    private String name;

    @Length(min = 1, max = 5)
    private String hand;

    /**
     * 嵌套验证
     */
    @Valid
    private Cat cat;


}
