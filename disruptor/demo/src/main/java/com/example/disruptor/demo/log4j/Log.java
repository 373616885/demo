package com.example.disruptor.demo.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author qinjp
 * @date 2019-07-06
 **/
public class Log {

    public static void main(String[] args) throws InterruptedException {
        Logger logger = LogManager.getLogger(Log.class);
        logger.trace("trace level");
        logger.debug("debug level");
        logger.info("info level");
        logger.warn("warn level");
        logger.error("error level");
        logger.fatal("fatal level");
        Thread.sleep(100000);
    }

}
