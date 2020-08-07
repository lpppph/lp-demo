package com.cn.lp;

/**
 * 数据库路由环境
 */
public class DataSourceRouterContext {

    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    private static String defaultDataSource = null;

    public static void setDefaultDataSource(String defaultDataSource) {
        DataSourceRouterContext.defaultDataSource = defaultDataSource;
    }

    public static void setDataSource(String dataSource) {
        holder.set(dataSource);
    }

    public static String getDataSource() {
        String lookUpKey = holder.get();
        return lookUpKey == null ? defaultDataSource : lookUpKey;
    }

    public static void clear() {
        holder.remove();
    }

}
