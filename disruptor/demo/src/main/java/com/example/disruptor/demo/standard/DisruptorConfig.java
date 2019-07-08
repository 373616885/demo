package com.example.disruptor.demo.standard;


import com.lmax.disruptor.*;

import java.util.concurrent.TimeUnit;

/**
 * @author qinjp
 * @date 2019-07-08
 **/
public final class DisruptorConfig {

    private DisruptorConfig() {
    }

    /**最小值*/
    private static final int RINGBUFFER_MIN_SIZE = 128;
    /**默认值*/
    private static final int RINGBUFFER_NO_GC_DEFAULT_SIZE = 4 * 1024;
    /**开启线程池的默认值*/
    private static final int RINGBUFFER_DEFAULT_SIZE = 256 * 1024;
    /**TimeoutBlockingWaitStrategy 超时10S**/
    private static final long DEFAULT_TIME_OUT = 10L;

    static int calculateRingBufferSize(final Integer userPreferredRBSize) {
        if (userPreferredRBSize == null){
            int ringBufferSize = isClassAvailable("javax.servlet.Servlet") ?
                    RINGBUFFER_NO_GC_DEFAULT_SIZE :
                    RINGBUFFER_DEFAULT_SIZE;
            if (ringBufferSize < RINGBUFFER_MIN_SIZE) {
                ringBufferSize = RINGBUFFER_MIN_SIZE;
            }
            return ceilingNextPowerOfTwo(ringBufferSize);
        }
        return ceilingNextPowerOfTwo(userPreferredRBSize);
    }

    static WaitStrategy createWaitStrategy(final String strategyUp) {
        return createWaitStrategy(strategyUp, DEFAULT_TIME_OUT);
    }

    static WaitStrategy createWaitStrategy(final String strategyUp, final long timeoutMillis) {
        // TODO Define a DisruptorWaitStrategy enum?
        switch (strategyUp) {
            case "SLEEP":
                return new SleepingWaitStrategy();
            case "YIELD":
                return new YieldingWaitStrategy();
            case "BLOCK":
                return new BlockingWaitStrategy();
            case "BUSYSPIN":
                return new BusySpinWaitStrategy();
            case "TIMEOUT":
                return new TimeoutBlockingWaitStrategy(timeoutMillis, TimeUnit.MILLISECONDS);
            default:
                return new TimeoutBlockingWaitStrategy(timeoutMillis, TimeUnit.MILLISECONDS);
        }
    }

    private static final int BITS_PER_INT = 32;

    /**
     * Calculate the next power of 2, greater than or equal to x.
     * <p>
     * From Hacker's Delight, Chapter 3, Harry S. Warren Jr.
     *
     * @param x Value to round up
     * @return The next power of 2 from x inclusive
     */
    private static int ceilingNextPowerOfTwo(final int x) {
        return 1 << (BITS_PER_INT - Integer.numberOfLeadingZeros(x - 1));
    }

    private static boolean isClassAvailable(final String className) {
        try {
            return Class.forName(className) != null;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

}
