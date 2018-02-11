package com.qin.spring.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {
	
	
	@GetMapping("/home")
	@ResponseBody
    public String home() {
        return "Hello World!";
    }
	
	@GetMapping("/success")
	 public String success() {
        return "success";
    }

}
