package com.siwuxie095.spring.chapter14th.example4th.cfg;

import com.siwuxie095.spring.chapter14th.example4th.service.JSR250SpittleService;
import com.siwuxie095.spring.chapter14th.example4th.service.SpittleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * @author Jiajing Li
 * @date 2021-03-07 16:46:53
 */
@SuppressWarnings("all")
@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class JSR250Config extends GlobalMethodSecurityConfiguration {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }

    @Bean
    public SpittleService spitterService() {
        return new JSR250SpittleService();
    }

}

