package com.qin.result.base;

import com.qin.result.common.ParamName;
import lombok.Data;

@Data
public class Job {

    @ParamName("jt")
    private String jobType;

    @ParamName("lt")
    private String location;

    private String name;

    protected String praram;

    /**
     *  that would work, but it's a but ugly
     */
    public String getNe() {
        return this.name;
    }

    public void setNe(String name) {
        this.name = name;
    }
}

