package com.cn.lp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据库路由AOP
 */
@Aspect
@Component
@Order(-1)
public class DataSourceRouterAOP {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceRouterAOP.class);

    /**
     * 此处的切点是注解的方式，也可以用包名的方式达到相同的效果
     * '@Pointcut("execution(* com.wwj.springboot.service.impl.*.*(..))")'
     */
    @Pointcut("@annotation(DataSourceRouter)")
    public void delayInvoke() {

    }


    /**
     * 环绕增强，相当于MethodInterceptor
     */
    @Around("delayInvoke()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取DataSourceRouter 注解获取数据源名字
        DataSourceRouter dataSourceRouter = method.getAnnotation(DataSourceRouter.class);
        // 通过DataSourceRouterContext指定当前数据源
        DataSourceRouterContext.setDataSource(dataSourceRouter.value());
        try {
            // 调用方法
            return joinPoint.proceed();
        } finally {
            // 清空环境
            DataSourceRouterContext.clear();
        }
    }

}
