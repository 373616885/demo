package com.zzsim.gz.airport.web.log.service;

import com.lmax.disruptor.EventHandler;
import com.zzsim.gz.airport.web.log.domain.LogEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 日志处理器
 *
 * @author qinjp
 * @date 2020/10/13
 */
@Slf4j
@Service
@AllArgsConstructor
public class LogHandler implements EventHandler<LogEvent> {

    private final LogService logService;

    /**
     * 异常队列 AsyncLogDisruptor 的处理方法
     */
    @Override
    public void onEvent(LogEvent event, long sequence, boolean endOfBatch) throws Exception {
        logService.log(event);
    }

}
