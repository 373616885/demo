package com.example.disruptor.demo.model;

import lombok.Data;

/**
 * @author qinjp
 * @date 2019-07-05
 **/
@Data
public class NotifyEvent {

    private Long seq;

    private String message;

}
