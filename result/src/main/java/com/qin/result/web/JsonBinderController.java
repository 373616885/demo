package com.qin.result.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qin.result.base.JsonParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JsonBinderController {

    @RequestMapping(method = RequestMethod.GET, value = "/json/job")
    public String myMethod(@RequestParam Map<String, Object> allRequestParams) {

        JsonParam param = new ObjectMapper().convertValue(allRequestParams, JsonParam.class);

        return param.toString();

    }
}
