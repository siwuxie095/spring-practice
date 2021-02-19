package com.siwuxie095.spring.chapter9th.example12th.cfg;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

/**
 * @author Jiajing Li
 * @date 2021-02-19 08:08:41
 */
@SuppressWarnings("all")
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .rememberMe()
                .tokenRepository(new InMemoryTokenRepositoryImpl())
                .tokenValiditySeconds(2419200)
                .key("spittrKey")
                .and()
                .httpBasic()
                .realmName("Spittr")
                .and()
                .authorizeRequests()
                .antMatchers("/").authenticated()
                .antMatchers("/spitter/me").authenticated()
                .antMatchers(HttpMethod.POST, "/spittles").authenticated()
                .anyRequest().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }

}

