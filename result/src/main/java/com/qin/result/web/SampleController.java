package com.qin.result.web;


import com.qin.result.config.DocumentServerProperties;
import com.qin.result.model.Third;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class SampleController {

    private final DocumentServerProperties documentServerProperties;

    private final Third third;

    @RequestMapping("/documentServerProperties")
    public Object getObjectProperties () {
        return documentServerProperties.toString() + " " + third.toString();
    }

}
