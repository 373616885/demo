package com.qin.demo.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class User {

    private String name;

    private Integer age;

    private List<Cat> cats;

    private Object cat;

}
