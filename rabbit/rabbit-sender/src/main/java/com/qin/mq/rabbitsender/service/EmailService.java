package com.qin.mq.rabbitsender.service;


public interface EmailService {

    void sendEmail(String message) throws Exception;
}