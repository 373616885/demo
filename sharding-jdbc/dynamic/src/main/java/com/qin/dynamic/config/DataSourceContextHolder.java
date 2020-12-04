package com.qin.dynamic.config;

/**
 * @author qinjp
 * @date 2020/12/4
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<String>();

    /**
     * 使用setDataSourceType设置当前的
     */
    public static void setDataSourceType(String dataSourceType) {
        CONTEXT.set(dataSourceType);
    }

    public static String getDataSourceType() {

        return CONTEXT.get();
    }

    public static void clearDataSourceType() {
        CONTEXT.remove();
    }

}
