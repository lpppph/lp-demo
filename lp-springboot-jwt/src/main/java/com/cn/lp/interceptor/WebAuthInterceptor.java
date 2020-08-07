package com.cn.lp.interceptor;

import com.cn.lp.AccountIdentifier;
import com.cn.lp.Permission;
import com.cn.lp.TokenInfo;
import com.cn.lp.annotations.NeedLogin;
import com.cn.lp.spel.SpelExcetor;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * web拦截器
 */
public abstract class WebAuthInterceptor implements HandlerInterceptor {

    protected static Logger logger = LoggerFactory.getLogger(WebAuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

        // huo
        Optional<HandlerMethod> methodOP = getHandlerMethod(handler);
        // 如果不是访问接口，直接成功
        if (!methodOP.isPresent()) {
            return true;
        }

        HandlerMethod method = methodOP.get();

        preMethodHandle(request, response, method);

        Optional<AccountIdentifier> IdentifierOp;

        TokenInfo token = this.getToken(request);
        // 如果有token，则验证有效性
        if (!token.isEmpty()) {
            // 校验和获取用户
            IdentifierOp = verifyAndGetIdentifier(token, request, response, method);
            IdentifierOp.ifPresent(accountIdentifier ->
                postGetAccount(request, response, accountIdentifier, token, method)
            );
            // 获取旧的用户
        } else {
            IdentifierOp = Optional.empty();
            // 构建新得用户
        }

        // 需要授权
        if (isNeedToAuth(method)) {
            if (!IdentifierOp.isPresent()) {
                if (logger.isErrorEnabled()) {
                    logger.error("接口权限验证失败, 未登录!");
                }
                // 未登录异常
                unAuthorizedProcess(token, request, response, method);
            }
            NeedLogin needLogin = method.getMethodAnnotation(NeedLogin.class);
            if (StringUtils.isNotEmpty(needLogin.permissionSpel())) {
                SpelExcetor spelExcetor = this.getSpelExcetor();
                boolean hasPermission = spelExcetor.doneInSpringContext(needLogin.permissionSpel());
                if (!hasPermission) {
                    if (logger.isErrorEnabled()) {
                        logger.error("接口权限验证失败, 权限不足");
                    }
                    // 权限不足
                    forbiddenProcess(token, request, response, method);
                }
            } else if (needLogin.permissions().length > 0) {
                List<Permission> permissions = getNeedPermissions(IdentifierOp.get(), needLogin.permissions(), method);
                if (!verifyPermissions(IdentifierOp.get(), permissions, token, request, response)) {
                    if (logger.isErrorEnabled()) {
                        logger.error(
                            "接口权限验证失败, 需要权限: {}",
                            Joiner.on(",").join(permissions.stream().map(Permission::getDesc).collect(Collectors.toList()))
                        );
                    }
                    // 权限不足
                    forbiddenProcess(token, request, response, method);
                }
            } else if (needLogin.enableInterfacePermission()) {
                String alias = needLogin.alias();
                if (StringUtils.isEmpty(alias)) {
                    alias = method.getMethod().getName();
                }
                Permission permission = getNeedPermissions(IdentifierOp.get(), alias, method);
                if (permission != null && !verifyPermissions(IdentifierOp.get(), Collections.singletonList(permission), token, request, response)) {
                    if (logger.isErrorEnabled()) {
                        logger.error("接口权限验证失败, 需要权限: {}", permission.getDesc());
                    }
                    // 权限不足
                    forbiddenProcess(token, request, response, method);
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("接口权限验证成功:[{}].", request.getServletPath());
            }

        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("接口不需要授权:[{}].", request.getServletPath());
            }
        }

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    protected abstract void preMethodHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod method);

    protected abstract TokenInfo getToken(HttpServletRequest request);

    protected abstract SpelExcetor getSpelExcetor();

    /**
     * 解密token
     *
     * @param token
     * @return
     */
//    protected TokenInfo decodeToken(TokenInfo token) {
//        return token;
//    }

    /**
     * 校验token并获取用户信息
     *
     * @param token
     * @param request
     * @param response
     * @param handler
     * @return
     */
    protected abstract Optional<AccountIdentifier> verifyAndGetIdentifier(TokenInfo token, HttpServletRequest request, HttpServletResponse response, HandlerMethod handler);

    /**
     * 未登录流程
     *
     * @param token
     * @param request
     * @param response
     * @param handler
     */
    protected abstract void unAuthorizedProcess(TokenInfo token, HttpServletRequest request, HttpServletResponse response, HandlerMethod handler);

    /**
     * 获取所需权限
     *
     * @param permissionIDs
     * @return
     */
    protected abstract List<Permission> getNeedPermissions(AccountIdentifier identifier, int[] permissionIDs, HandlerMethod handler);

    /**
     * 获取所需权限
     *
     * @param permissionName
     * @return
     */
    protected abstract Permission getNeedPermissions(AccountIdentifier identifier, String permissionName, HandlerMethod handler);

    /**
     * 校验权限
     *
     * @param permissions
     * @param request
     * @param response
     * @return
     */
    protected abstract boolean verifyPermissions(AccountIdentifier identifier, List<Permission> permissions, TokenInfo tokenInfo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 拒绝流程
     *
     * @param token
     * @param request
     * @param response
     * @param handler
     */
    protected abstract void forbiddenProcess(TokenInfo token, HttpServletRequest request, HttpServletResponse response, HandlerMethod handler);


    /**
     * 获取账户后
     *
     * @param token
     * @param request
     * @param response
     * @param handler
     */
    protected void postGetAccount(HttpServletRequest request, HttpServletResponse response, AccountIdentifier identifier, TokenInfo token, HandlerMethod handler) {

    }

    /**
     * 判断是否是Controller方法
     *
     * @param handler
     * @return
     */
    private Optional<HandlerMethod> getHandlerMethod(Object handler) {
        if (handler instanceof HandlerMethod) {
            return Optional.of((HandlerMethod) handler);
        }
        return Optional.empty();
    }

    /**
     * 是否需要授权
     *
     * @param handler Controller接口方法
     * @return 是否需要授权
     */
    protected boolean isNeedToAuth(HandlerMethod handler) {
        return handler.hasMethodAnnotation(NeedLogin.class);
    }

}
