package com.cn.lp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({DataSourceConfiguration.class})
@EntityScan("com.cn.lp")
@EnableJpaAuditing
@EnableJpaRepositories("com.cn.lp")
public class LpDatasourceRouterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LpDatasourceRouterApplication.class, args);
    }

}
