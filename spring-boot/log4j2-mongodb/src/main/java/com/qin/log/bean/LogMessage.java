package com.qin.log.bean;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Data
public class LogMessage {
    private ObjectId _id;
    private String level;
    private String loggerName;
    private String message;
    private String source;
    private String marker;
    private Long threadId;
    private String threadName;
    private Integer threadPriority;
    private Long millis;
    private Date date;
    private String thrown;
    private ContextMap contextMap;
    private List<String> contextStack;
}
