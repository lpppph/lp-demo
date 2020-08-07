package com.cn.lp.test.domain;

import com.cn.lp.spel.SpelExcetor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by qirong on 2019/5/31.
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private CommonWebAuthInterceptor webAuthInterceptor;

    @Bean
    public SpelExcetor getSpelExcetor() {
        return new SpelExcetor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webAuthInterceptor);
    }

}
