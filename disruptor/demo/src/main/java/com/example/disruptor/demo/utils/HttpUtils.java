package com.example.disruptor.demo.utils;

import com.example.disruptor.demo.model.LoginReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qinjp
 * @date 2019-07-05
 **/
public class HttpUtils {
    public static void main(String[] args) throws JsonProcessingException {

        //headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("content-type", "application/json");

        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();

        //body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("mobile", "18594201848");
        requestBody.put("smsCode", "666666");
        requestBody.put("deviceNo", "987654321");
        requestBody.put("pmodel", "xiaomi");

        LoginReq req = new LoginReq();
        req.setMobile("18594201848");
        req.setDeviceNo("987654321");
        req.setSmsCode("666666");
        req.setPmodel("xiaomi");

        ObjectMapper mapper = new ObjectMapper();

        //HttpEntity
        HttpEntity<String> requestEntity = new HttpEntity<>(mapper.writeValueAsString(req), requestHeaders);


        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8001/user/login";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getHeaders());
        System.out.println(responseEntity.getBody());

    }

}
