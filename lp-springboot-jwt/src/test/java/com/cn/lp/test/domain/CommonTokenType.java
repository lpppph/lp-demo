package com.cn.lp.test.domain;

import com.cn.lp.TokenType;

/**
 * Created by qirong on 2020/6/14.
 */
public enum CommonTokenType implements TokenType {

    NORMAL(1);

    private int id;

    CommonTokenType(int id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

}
