package com.cn.lp.test.domain;

import org.springframework.stereotype.Service;

@Service("test")
public class TestService {

    public boolean test(String msg) {
        System.out.println(msg);
        return true;
    }

}
