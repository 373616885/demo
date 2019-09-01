package com.qin.redis.pc;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
@Component
@AllArgsConstructor
public class Consumer implements CommandLineRunner {

    private static final LongAdder adder = new LongAdder();

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
        Thread thread = new Thread(() -> runWait());
        // 守护线程--是否后台运行
        thread.setDaemon(true);
        thread.setName("redis-consumer");
        thread.start();
    }

    private void runWait() {
        while (true) {
            adder.increment();
            try {
                // 阻塞式brpop，List中无数据时阻塞，参数0表示一直阻塞下去，直到List出现数据
                String result = stringRedisTemplate.opsForList().rightPop(Constant.PRODUCER_CONSUMER, 1, TimeUnit.SECONDS);
                log.warn("消费者次数：{} 结果：{}", adder.intValue(), result);
                synchronized (this) {
                    // 10 秒
                    this.wait(10000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 消费者
     */
    private void run() {
        while (true) {
            adder.increment();
            try {
                // 阻塞式brpop，List中无数据时阻塞，参数0表示一直阻塞下去，直到List出现数据
                String result = stringRedisTemplate.opsForList().rightPop(Constant.PRODUCER_CONSUMER, 20, TimeUnit.SECONDS);
                log.warn("消费者次数：{} 结果：{}", adder.intValue(), result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
