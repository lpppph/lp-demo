package com.cn.lp;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态DataSource路由
 */
public class DynamicRouterDataSource extends AbstractRoutingDataSource {

    private Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceRouterContext.getDataSource();
    }

    public boolean addDataSource(String key, DataSource dataSource) {
        DataSource oldDataSource = (DataSource) dataSourceMap.putIfAbsent(key, dataSource);
        if (oldDataSource == null) {
            return true;
        }
        return false;
    }

    public void removeDataSource(String key) {
        dataSourceMap.remove(key);
    }


}
