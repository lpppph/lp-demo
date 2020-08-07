package com.cn.lp;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by qirong on 2020/6/14.
 */
public class TokenInfo {

    private String token;

    private TokenType type;

    public static final TokenInfo EMPTY = new TokenInfo();

    public static TokenInfo ofEmpty() {
        return EMPTY;
    }

    public static TokenInfo of(String token, TokenType tokenType) {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.token = token;
        tokenInfo.type = tokenType;
        return tokenInfo;
    }

    public boolean isEmpty() {
        return StringUtils.isBlank(this.token);
    }

    public String getToken() {
        return token;
    }

    public TokenType getType() {
        return type;
    }

}
