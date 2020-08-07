package com.cn.lp.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 测试实例DAO
 * Created by on 2019/7/15.
 */
@Repository
public interface TestEntityDAO extends JpaRepository<TestEntity, Long>, JpaSpecificationExecutor<TestEntity> {

}
