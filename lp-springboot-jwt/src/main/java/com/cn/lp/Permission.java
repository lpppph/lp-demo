package com.cn.lp;

/**
 * 权限
 * Created by qirong on 2019/5/31.
 */
public interface Permission {

    /**
     * 获得id
     */
    Integer getId();

    /**
     * 获得名字
     */
    String getName();

    /**
     * 获得描述
     * @return
     */
    String getDesc();

}
