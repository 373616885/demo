package com.zzsim.gz.airport.web.log.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmax.disruptor.EventHandler;
import com.zzsim.gz.airport.web.log.domain.LogEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author qinjp
 * @date 2020/10/13
 */
@Slf4j
@Service
@AllArgsConstructor
public class LogHandler implements EventHandler<LogEvent> {

    private final ObjectMapper objectMapper;

    @Override
    public void onEvent(LogEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.warn(objectMapper.writeValueAsString(event));
    }
}
