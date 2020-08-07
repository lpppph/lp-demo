package com.cn.lp.test.domain.role;

import com.cn.lp.AccountIdentifier;

/**
 * Created by on 2019/8/9.
 */
public class AccountMeta implements AccountIdentifier {

    public static String UID_PARAM_NAME = "uid";

    public static String ACCOUNT_NAME_PARAM_NAME = "accountName";

    private long uid;

    private String accountName;

    public static AccountMeta of(long uid, String accountName) {
        AccountMeta accountMeta = new AccountMeta();
        accountMeta.uid = uid;
        accountMeta.accountName = accountName;
        return accountMeta;
    }

    public Long getUid() {
        return uid;
    }

    public String getAccountName() {
        return accountName;
    }
}
