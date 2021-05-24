package com.qin.log.bean;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Person {

    private ObjectId id;

    private String name;

    private int age;

    private Address address;

}
