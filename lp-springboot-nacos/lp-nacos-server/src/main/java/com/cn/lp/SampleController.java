package com.cn.lp;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class SampleController {

    @Value("${user.name}")
    private String userName;

    @Value("${user.age}")
    private int age;

    @NacosInjected
    private NamingService namingService;

    @RequestMapping("/user")
    public String simple() throws NacosException {
//        namingService.subscribe("", event -> {
//
//        });
        return "获取 Nacos Config配置如下：" + userName + " " + age + "!";
    }

}
