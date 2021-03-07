package com.siwuxie095.spring.chapter14th.example3rd.cfg;

import com.siwuxie095.spring.chapter14th.example3rd.service.SecuredSpittleService;
import com.siwuxie095.spring.chapter14th.example3rd.service.SpittleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * @author Jiajing Li
 * @date 2021-03-07 16:19:25
 */
@SuppressWarnings("all")
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecuredConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }

    @Bean
    public SpittleService spitterService() {
        return new SecuredSpittleService();
    }

}