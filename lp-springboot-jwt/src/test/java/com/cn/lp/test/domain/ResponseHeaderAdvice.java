package com.cn.lp.test.domain;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截Controller方法默认返回参数，统一处理返回值/响应体
 * Created by qirong on 2019/8/10.
 */
@ControllerAdvice
public class ResponseHeaderAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
        ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) serverHttpRequest;
        ServletServerHttpResponse serverResponse = (ServletServerHttpResponse) serverHttpResponse;
        if (serverRequest == null || serverResponse == null
            || serverRequest.getServletRequest() == null || serverResponse.getServletResponse() == null) {
            return o;
        }
        // 对于未添加跨域消息头的响应进行处理
        HttpServletRequest request = serverRequest.getServletRequest();
        HttpServletResponse response = serverResponse.getServletResponse();
        crossDomain(request, response);
        return o;
    }

    /**
     * 跨域支持
     */
    private void crossDomain(HttpServletRequest request, HttpServletResponse response) {
        String originHeader = "Access-Control-Allow-Origin";
        if (!response.containsHeader(originHeader)) {
            String origin = request.getHeader("Origin");
            if (origin == null) {
                String referer = request.getHeader("Referer");
                if (referer != null)
                    origin = referer.substring(0, referer.indexOf("/", 7));
            }
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        String allowHeaders = "Access-Control-Allow-Headers";
        if (!response.containsHeader(allowHeaders))
            response.setHeader(allowHeaders, request.getHeader(allowHeaders));

        String allowMethods = "Access-Control-Allow-Methods";
        if (!response.containsHeader(allowMethods))
            response.setHeader(allowMethods, "GET,POST,OPTIONS,HEAD");
        //这个很关键，要不然ajax调用时浏览器默认不会把这个token的头属性返给JS
        String exposeHeaders = "access-control-expose-headers";
        if (!response.containsHeader(exposeHeaders))
            response.setHeader(exposeHeaders, "x-auth-token");
    }

}