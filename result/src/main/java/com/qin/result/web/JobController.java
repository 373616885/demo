package com.qin.result.web;

import com.qin.result.base.Job;
import com.qin.result.base.JobParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

    @GetMapping("/job")
    public String job(@RequestParam Job job){
        return job.toString();
    }

    @GetMapping("/job/param")
    public String job(JobParam job){
        return job.toString();
    }

}
