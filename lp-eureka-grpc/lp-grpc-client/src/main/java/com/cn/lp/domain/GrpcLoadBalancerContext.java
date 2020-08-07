package com.cn.lp.domain;

import io.grpc.Attributes;

import java.util.Optional;

public class GrpcLoadBalancerContext extends ThreadLocal<GrpcLoadBalanceAttributes> {

    public static final GrpcLoadBalancerContext instance = new GrpcLoadBalancerContext();

    @Override
    protected GrpcLoadBalanceAttributes initialValue() {
        return new GrpcLoadBalanceAttributes();
    }

    public static <V> void setAttribute(Attributes.Key<V> key, V value) {
        instance.get().setAttribute(key, value);
    }

    public static <V> Optional<V> getAttribute(Attributes.Key<V> key) {
        return instance.get().getAttribute(key);
    }

    public static void clear() {
        instance.get().clear();
    }

    public static void setLazyClear() {
        instance.get().lazyClear();
    }

    public static void lazyClear() {
        if (!instance.get().isLazyClear()) {
            instance.clear();
        }
    }
}
