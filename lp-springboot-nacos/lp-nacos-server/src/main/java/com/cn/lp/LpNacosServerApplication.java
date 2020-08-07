package com.cn.lp;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@EnableDiscoveryClient
@SpringBootApplication
public class LpNacosServerApplication {

    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(LpNacosServerApplication.class);

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(LpNacosServerApplication.class, args);
        while (true) {
            //当动态配置刷新时，会更新到 Enviroment中，因此这里每隔一秒中从Enviroment中获取配置
            String userName = applicationContext.getEnvironment().getProperty("user.name");
            String userAge = applicationContext.getEnvironment().getProperty("user.age");
            String addr = applicationContext.getEnvironment().getProperty("user.addr");
            logger.info("配置信息，名称：{}，年龄：{}, 地址: {}", userName, userAge, addr);
            TimeUnit.SECONDS.sleep(1);
        }
    }

}
