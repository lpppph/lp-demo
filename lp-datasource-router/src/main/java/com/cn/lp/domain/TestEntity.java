package com.cn.lp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 测试实例
 * Created by on 2019/7/15.
 */
@Entity
@Table(name = "test")
public class TestEntity {

    @Id
    private long id;

    @Column(length = 50)
    private String name;

    @Column
    private int sign;

    @Column(nullable = true)
    private Long time;

    public static TestEntity of(long id, String name, int sign) {
        TestEntity testEntity = new TestEntity();
        testEntity.id = id;
        testEntity.name = name;
        testEntity.sign = sign;
        return testEntity;
    }

    public void updateTime(long time) {
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    public int getSign() {
        return sign;
    }

    public TestEntity setSign(int sign) {
        this.sign = sign;
        return this;
    }

    public long getId() {
        return id;
    }

    public TestEntity setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TestEntity setName(String name) {
        this.name = name;
        return this;
    }

}
