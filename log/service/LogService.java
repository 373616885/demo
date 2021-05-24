package com.zzsim.gz.airport.web.log.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zzsim.gz.airport.mybatis.admin.dao.AccountMapper;
import com.zzsim.gz.airport.mybatis.admin.dao.RoleMapper;
import com.zzsim.gz.airport.mybatis.admin.entity.Account;
import com.zzsim.gz.airport.mybatis.admin.entity.Role;
import com.zzsim.gz.airport.mybatis.business.dao.LogMapper;
import com.zzsim.gz.airport.mybatis.business.entity.Log;
import com.zzsim.gz.airport.web.jwt.JwtUtils;
import com.zzsim.gz.airport.web.log.domain.LogEvent;
import com.zzsim.gz.airport.web.log.domain.LogHandlerTypeBo;
import com.zzsim.gz.airport.web.log.domain.LogUserInfoBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author qinjp
 * @date 2020/10/13
 */
@Slf4j
@Service
@AllArgsConstructor
public class LogService {
    private final ObjectMapper objectMapper;

    private final AccountMapper accountMapper;

    private final RoleMapper roleMapper;

    private final LogMapper logMapper;

    private final HandlerType handlerContent;

    /**
     * 缓存用户信息两分钟--超两分钟没有操作才更新
     */
    private final Cache<String, LogUserInfoBo> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .initialCapacity(256)
            .maximumSize(5120)
            .build();

    /**
     * 操作日志处理
     */
    public void log(LogEvent event) throws JsonProcessingException, ExecutionException {

        Log log = new Log();

        log.setEquence(event.getEquence());
        log.setHost(event.getHost());
        log.setUrl(event.getUrl());
        log.setParam(objectMapper.writeValueAsString(event.getParam()));
        log.setResult(objectMapper.writeValueAsString(event.getResult()));

        /*
         * 不要登录的接口处理
         */
        final String token = event.getToken();

        if (StringUtils.isNotBlank(token)) {
            /*
             * logBo（用户的一些信息） 缓存2分钟
             */
            LogUserInfoBo logBo = cache.get(token, () -> {
                LogUserInfoBo transit = handlerToken(token);
                cache.put(token, transit);
                return transit;
            });
            log.setUsername(logBo.getUsername());
            log.setRoleName(logBo.getRoleName());
            log.setMobile(logBo.getMobile());
            log.setCreater(String.valueOf(logBo.getAccountId()));

        }

        LogHandlerTypeBo holder = handler(event.getUrl());
        // 操作类型
        log.setType(holder.getType());
        // 操作内容
        log.setOperation(holder.getHandler().apply(event.getUrl(), event.getParam()));

        logMapper.insert(log);
    }


    /**
     * 通过token获取必要的信息
     */
    private LogUserInfoBo handlerToken(String token) {

        Integer accountId = JwtUtils.decrypt(token);

        Account account = accountMapper.selectById(accountId);

        Role role = roleMapper.selectById(account.getRoleId());

        return LogUserInfoBo.builder()
                .accountId(account.getId())
                .username(account.getUsername())
                .roleName(role.getRoleName())
                .mobile(account.getMobile())
                .build();
    }

    private LogHandlerTypeBo handler(String url) {
        return handlerContent.getHandlerType(url);
    }


}
