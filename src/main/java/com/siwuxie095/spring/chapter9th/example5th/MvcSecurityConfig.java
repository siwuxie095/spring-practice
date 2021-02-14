package com.siwuxie095.spring.chapter9th.example5th;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

/**
 * 为 Spring MVC 启用 Web 安全性功能的最简单配置
 *
 * @author Jiajing Li
 * @date 2021-02-14 10:30:37
 */
@SuppressWarnings("all")
@Configuration
@EnableWebMvcSecurity
public class MvcSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }

}
