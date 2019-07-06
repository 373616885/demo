package com.example.disruptor.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author qinjp
 * @date 2019-07-06
 **/
@Setter
@Getter
@ToString
public class LongEvent {

    private Long value;
    private int data;

}
