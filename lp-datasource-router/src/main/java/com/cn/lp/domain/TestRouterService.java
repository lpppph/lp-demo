package com.cn.lp.domain;

import com.cn.lp.DataSourceRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 测试路由服务
 */
@Service
public class TestRouterService {

    @Autowired
    private TestEntityDAO testEntityDAO;

    @DataSourceRouter(value = "masterDataSource")
    public void testD1() {
        TestEntity entity = TestEntity.of(System.currentTimeMillis(), "123", 1);
        testEntityDAO.save(entity);
    }

    @DataSourceRouter(value = "slaveDataSource")
    public void testD2() {
        TestEntity entity = TestEntity.of(System.currentTimeMillis(), "123", 1);
        testEntityDAO.save(entity);
    }

}
