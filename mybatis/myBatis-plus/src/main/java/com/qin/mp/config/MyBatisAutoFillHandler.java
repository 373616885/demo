package com.qin.mp.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

@Slf4j
public class MyBatisAutoFillHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        // IP
        if (metaObject.hasSetter(HOST)) {
            setFieldValByName(HOST, "127.0.0.1", metaObject);
        }
        // 创建人
        if (metaObject.hasSetter(CREATER)) {
            setFieldValByName(CREATER, "qinjp", metaObject);
        }
        // 更新人
        if (metaObject.hasSetter(MODIFIER)) {
            setFieldValByName(MODIFIER, "qinjp", metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间
        if (metaObject.hasSetter(GMTMODIFIED)) {
            setFieldValByName(GMTMODIFIED, LocalDateTime.now(), metaObject);
        }
        // 更新人
        if (metaObject.hasSetter(MODIFIER)) {
            setFieldValByName(MODIFIER, "qinjp", metaObject);
        }
    }

    private static final String HOST = "host";
    private static final String CREATER = "creater";
    private static final String MODIFIER = "modifier";
    private static final String GMTMODIFIED = "gmtModified";
}
