package com.qin.result.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonParam {

    @JsonProperty("jt")
    private String jobType;

    @JsonProperty("lt")
    private String location;

    @JsonProperty("ne")
    private String name;

    @JsonProperty("pm")
    protected String praram;

}
