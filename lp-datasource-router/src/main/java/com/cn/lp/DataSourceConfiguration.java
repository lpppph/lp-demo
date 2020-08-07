package com.cn.lp;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Datasource 配置
 */
@Configuration
public class DataSourceConfiguration {

    private final static String FIRST_DATASOURCE_KEY = "firstDataSource";
    private final static String SECOND_DATASOURCE_KEY = "secondDataSource";

    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    @Bean(value = FIRST_DATASOURCE_KEY)
    @Qualifier(FIRST_DATASOURCE_KEY)
    // ConfigurationProperties 指定属性前缀，忽略那些不能绑定到 @ConfigurationProperties 类字段的属性
    @ConfigurationProperties(prefix = "spring.datasource.d1")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(value = SECOND_DATASOURCE_KEY)
    @Qualifier(SECOND_DATASOURCE_KEY)
    // ConfigurationProperties 指定属性前缀，忽略那些不能绑定到 @ConfigurationProperties 类字段的属性
    @ConfigurationProperties(prefix = "spring.datasource.d2")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Primary
    @Bean(name = "routingDataSource")
    public AbstractRoutingDataSource routingDataSource(
        @Qualifier(FIRST_DATASOURCE_KEY) DataSource d1,
        @Qualifier(SECOND_DATASOURCE_KEY) DataSource d2
    ) {
        DynamicRouterDataSource proxy = new DynamicRouterDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put("masterDataSource", d1);
        targetDataSources.put("slaveDataSource", d2);
        proxy.setDefaultTargetDataSource(d1);
        proxy.setTargetDataSources(targetDataSources);
        DataSourceRouterContext.setDefaultDataSource("masterDataSource");
        return proxy;
    }

}
