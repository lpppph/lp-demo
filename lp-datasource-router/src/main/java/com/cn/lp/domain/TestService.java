package com.cn.lp.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 测试服务
 */
@Service
public class TestService {

    @Autowired
    private TestRouterService routerService;

    @PostConstruct
    public void test() {
        routerService.testD1();
        routerService.testD2();
    }

}
