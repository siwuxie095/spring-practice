package com.siwuxie095.spring.chapter9th.example7th;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

/**
 * 配置 Spring Security 使用内存用户存储
 *
 * @author Jiajing Li
 * @date 2021-02-16 22:20:56
 */
@SuppressWarnings("all")
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .and()
                .withUser("admin").password("password").roles("USER", "ADMIN");
        // 二者等价
        auth.inMemoryAuthentication()
                .withUser("user").password("password").authorities("ROLE_USER")
                .and()
                .withUser("admin").password("password").authorities("ROLE_USER", "ROLE_ADMIN");
    }

}
