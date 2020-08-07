package com.cn.lp.domain;

import io.grpc.Attributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GrpcLoadBalanceAttributes {

    public static final Attributes.Key<String> USER_IP_KEY = Attributes.Key.create("user_ip");

    private boolean lazyClear;

    private Map<String, Object> attributeMap = new HashMap<>();

    public <V> void setAttribute(Attributes.Key<V> key, V value) {
        attributeMap.put(key.toString(), value);
    }

    public <V> Optional<V> getAttribute(Attributes.Key<V> key) {
        return Optional.ofNullable((V) attributeMap.get(key.toString()));
    }

    public void clear() {
        this.attributeMap.clear();
        lazyClear = false;
    }

    public boolean isLazyClear() {
        return lazyClear;
    }

    public void lazyClear() {
        lazyClear = true;
    }

}
