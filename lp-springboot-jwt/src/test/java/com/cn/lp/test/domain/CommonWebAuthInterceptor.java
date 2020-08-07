package com.cn.lp.test.domain;

import com.cn.lp.AccountIdentifier;
import com.cn.lp.Permission;
import com.cn.lp.TokenInfo;
import com.cn.lp.interceptor.WebAuthInterceptor;
import com.cn.lp.spel.SpelExcetor;
import com.cn.lp.test.domain.exception.AuthException;
import com.cn.lp.test.domain.role.AccountManager;
import com.cn.lp.test.domain.role.AccountMeta;
import com.cn.lp.test.domain.role.AccountPermission;
import com.cn.lp.utils.JwtUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * web拦截器的基础实现
 * Created by qirong on 2019/5/31.
 */
@Component
public class CommonWebAuthInterceptor extends WebAuthInterceptor {

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private SpelExcetor spelExcetor;

    public static final String ACCESS_TOKEN_REQUEST_ATT_NAME = "access_token";

    public static final String ACCESS_TOKEN_HEADER_NAME = "lp_token";

    public static final String CHECK_MD5 = "lp_check_md5";

    @Autowired
    private AuthConfig authConfig;

    private static final List<String> WRITE_URLS = Lists.newArrayList("/test");

    /**
     * 校验token并获取用户信息
     *
     * @param token
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    protected Optional<AccountIdentifier> verifyAndGetIdentifier(TokenInfo token, HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        if (JwtUtils.verifyToken(token.getToken(), authConfig.getSecretKey())) {
            Map<String, String> claimMap = JwtUtils.getClaimMap(token.getToken());
            Long uid = Long.valueOf(claimMap.get(AccountMeta.UID_PARAM_NAME));
            return Optional.of(AccountMeta.of(uid, claimMap.get(AccountMeta.ACCOUNT_NAME_PARAM_NAME)));
        }
        return Optional.empty();
    }

    @Override
    protected void preMethodHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod method) {
        if (!request.getMethod().equals("GET")) {
            if (!WRITE_URLS.contains(request.getServletPath())) {
                String md5 = request.getHeader(CHECK_MD5);
                request.setAttribute("request_md5", md5);
            }
        }
    }

    @Override
    protected TokenInfo getToken(HttpServletRequest request) {
        String token = request.getHeader(ACCESS_TOKEN_HEADER_NAME);
        if (StringUtils.isBlank(token)) {
            return TokenInfo.ofEmpty();
        }
        return TokenInfo.of(token, CommonTokenType.NORMAL);
    }


    @Override
    protected SpelExcetor getSpelExcetor() {
        return spelExcetor;
    }

    /**
     * 未登录流程
     *
     * @param token
     * @param request
     * @param response
     * @param handler
     */
    @Override
    protected void unAuthorizedProcess(TokenInfo token, HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        throw new AuthException("未登录");
    }

    @Override
    protected List<Permission> getNeedPermissions(AccountIdentifier identifier, int[] permissionIDs, HandlerMethod handler) {
        List<Permission> permissionList = new ArrayList<>();
        // 获取注解上所有权限
        for (int id : permissionIDs) {
            // 将权限转换为枚举
            AccountPermission permission = AccountPermission.getByID(id);
            Assert.notNull(permission, "权限转换错误, 权限ID :" + id + "，方法 : " + handler.getMethod().getName());
            permissionList.add(permission);
        }
        return permissionList;
    }

    @Override
    protected Permission getNeedPermissions(AccountIdentifier identifier, String permissionName, HandlerMethod handler) {
        // 将权限转换为枚举
        AccountPermission permission = AccountPermission.getByName(permissionName);
        Assert.notNull(permission, "权限转换错误, 权限name :" + permissionName + "，方法 : " + handler.getMethod().getName());
        return permission;
    }

    /**
     * 校验权限
     *
     * @param accountIdentifier
     * @param needPermissions
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean verifyPermissions(AccountIdentifier accountIdentifier, List<Permission> needPermissions, TokenInfo tokenInfo, HttpServletRequest request, HttpServletResponse response) {
        // 从账户对象上获取权限列表
        AccountMeta accountMeta = (AccountMeta) accountIdentifier;
        List<Permission> hadPermissionList = accountManager.getAccount(accountMeta.getUid()).getPermissions();
        for (Permission permission : needPermissions) {
            // 判断权限列表是否包含所需权限
            if (!hadPermissionList.contains(permission)) {
                return false;
            }
        }
        // 成功
        return true;
    }

    /**
     * 拒绝流程
     *
     * @param token
     * @param request
     * @param response
     * @param handler
     */
    @Override
    protected void forbiddenProcess(TokenInfo token, HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        throw new AuthException("没有权限，拒绝登录");
    }

    @Override
    protected void postGetAccount(HttpServletRequest request, HttpServletResponse response, AccountIdentifier identifier, TokenInfo token, HandlerMethod handler) {
        super.postGetAccount(request, response, identifier, token, handler);
        request.setAttribute(ACCESS_TOKEN_REQUEST_ATT_NAME, token);
        request.setAttribute("user", identifier);
    }

}
