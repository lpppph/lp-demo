package com.cn.lp.test.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by qirong on 2019/8/10.
 */
@Configuration
public class AuthConfig {

    @Value("${server.auth.secretKey: lp1234}")
    private String secretKey;

    @Value("${server.auth.jwt.refreshSpace: 30000}")
    private long tokenRefreshSpace;

    public String getSecretKey() {
        return secretKey;
    }

    public AuthConfig setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public long getTokenRefreshSpace() {
        return tokenRefreshSpace;
    }

    public AuthConfig setTokenRefreshSpace(long tokenRefreshSpace) {
        this.tokenRefreshSpace = tokenRefreshSpace;
        return this;
    }
}
