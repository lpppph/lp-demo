package com.cn.lp;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * 数据库路由注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional(propagation = Propagation.REQUIRES_NEW)
public @interface DataSourceRouter {

    String value();

}
