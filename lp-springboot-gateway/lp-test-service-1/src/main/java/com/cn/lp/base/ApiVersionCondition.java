package com.cn.lp.base;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    /**
     * 接口路径中的版本号前缀，如: api/v[1-n]/test
     */
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("/v(\\d+)/");

    private int apiVersion;

    ApiVersionCondition(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        // 最近优先原则，方法定义的 @ApiVersion > 类定义的 @ApiVersion
        return new ApiVersionCondition(other.getApiVersion());
    }

    /**
     * 检查当前请求匹配条件和指定请求request是否匹配，如果不匹配返回null，
     * 如果匹配，生成一个新的请求匹配条件，该新的请求匹配条件是当前请求匹配条件
     * 针对指定请求request的剪裁。
     * 举个例子来讲，如果当前请求匹配条件是一个路径匹配条件，包含多个路径匹配模板，
     * 并且其中有些模板和指定请求request匹配，那么返回的新建的请求匹配条件将仅仅
     * 包含和指定请求request匹配的那些路径模板。
     * @param request
     * @return
     */
    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURI());
        if (m.find()) {
            // 获得符合匹配条件的ApiVersionCondition
            int version = Integer.valueOf(m.group(1));
            if (version >= getApiVersion()) {
                return this;
            }
        }
        return null;
    }

    /**
     * 针对指定的请求对象request比较两个请求匹配条件。
     * 该方法假定被比较的两个请求匹配条件都是针对该请求对象request调用了
     * #getMatchingCondition方法得到的，这样才能确保对它们的比较
     * 是针对同一个请求对象request，这样的比较才有意义(最终用来确定谁是
     * 更匹配的条件)。
     *
     * @param other
     * @param request
     * @return
     */
    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // 当出现多个符合匹配条件的ApiVersionCondition，优先匹配版本号较大的
        return other.getApiVersion() - getApiVersion();
    }

    public int getApiVersion() {
        return apiVersion;
    }
}
