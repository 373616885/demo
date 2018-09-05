package com.qin.mq.rabbitsender.controller;

import com.alibaba.fastjson.JSONObject;
import com.qin.mq.rabbitsender.domain.MailMessageModel;
import com.qin.mq.rabbitsender.service.EmailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 */
@RestController()
@RequestMapping(value = "/v1/emails")
public class EmailController {

    @Resource
    private EmailService emailService;

    /**
     * 新增方法
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public MailMessageModel add() throws Exception {
        /* json结构体
        {
            "to":"xxx@163.com",
            "subject":"xxx",
            "text":"<html><head></head><body><h1>邮件测试</h1><p>hello!this is mail test。</p></body></html>"
        }
         */
        MailMessageModel model = new MailMessageModel();
        model.setTo("373616885@qq.com");
        model.setSubject("rabbit-sender");
        model.setText("<html><head></head><body><h1>邮件测试</h1><p>hello!this is mail test。</p></body></html>");
        emailService.sendEmail(JSONObject.toJSONString(model));
        return model;
    }
}