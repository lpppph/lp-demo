package com.cn.lp.test.domain;

import com.cn.lp.common.utils.JSONAide;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by qirong on 2020/3/18.
 */
@Aspect
@Component
public class TestAspect {

    /**
     * 此处的切点是注解的方式，也可以用包名的方式达到相同的效果
     * '@Pointcut("execution(* com.wwj.springboot.service.impl.*.*(..))")'
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
        "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
        "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
        "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
        "@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void delayInvoke() {

    }

    /**
     * 环绕增强，相当于MethodInterceptor
     */
    @Around("delayInvoke()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        Annotation[][] annotations = method.getParameterAnnotations();
        boolean find = false;
        int index = 0;
        for(; index < annotations.length; index ++) {
            if (annotations[index] != null && annotations[index].length > 0) {
                for (Annotation annotation : annotations[index]) {
                    if (annotation instanceof RequestBody) {
                        find = true;
                        break;
                    }
                }
            }
            if(find) {
                break;
            }
        }
        if(find) {
            Object body = joinPoint.getArgs()[index];
            System.out.println(JSONAide.toJson(body));
        }
        Object result = joinPoint.proceed();
        return result;
//        HttpServletRequest request = sra.getRequest();
//        String url = request.getRequestURL().toString();
//        String method = request.getMethod();
//        String uri = request.getRequestURI();
//        String queryString = request.getQueryString();
        // 获取@RequestBody注解
        //这里可以获取到get请求的参数和其他信息
    }

}
