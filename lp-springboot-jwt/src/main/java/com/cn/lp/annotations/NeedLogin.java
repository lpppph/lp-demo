package com.cn.lp.annotations;


import java.lang.annotation.*;

/**
 * 此注解用于Controller接口方法上，标记为需要登录
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NeedLogin {

    /**
     * 接口别名
     */
    String alias() default "";

    /**
     * 许可表达式
     *
     * @return
     */
    String permissionSpel() default "";

    /**
     * 权限列表
     *
     * @return
     */
    int[] permissions() default {};

    /**
     * 是否开启接口权限， 根据接口名校验权限
     * @return
     */
    boolean enableInterfacePermission() default false;

}
