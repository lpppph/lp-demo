package com.cn.lp.test.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by qirong on 2020/3/9.
 */
@ApiModel(value = "LBALoginAccountDTO", description = "登录")
public class LBALoginAccountDTO {

    @ApiModelProperty(value = "账号", required = true)
    private String accountName;

    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "验证码", required = true)
    private String code;

    @ApiModelProperty(value = "验证码唯一ID", required = true)
    private String uuid;

    public String getAccountName() {
        return accountName;
    }

    public LBALoginAccountDTO setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LBALoginAccountDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getCode() {
        return code;
    }

    public LBALoginAccountDTO setCode(String code) {
        this.code = code;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public LBALoginAccountDTO setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
}
