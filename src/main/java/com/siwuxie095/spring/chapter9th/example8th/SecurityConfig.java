package com.siwuxie095.spring.chapter9th.example8th;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.sql.DataSource;

/**
 * @author Jiajing Li
 * @date 2021-02-16 22:43:45
 */
@SuppressWarnings("all")
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, true " +
                        "from Spitter where username=?")
                .authoritiesByUsernameQuery("select username, 'ROLE_USER' " +
                        "from Spitter where username=?")
                .passwordEncoder(new StandardPasswordEncoder("53cr3t"));
    }

}
