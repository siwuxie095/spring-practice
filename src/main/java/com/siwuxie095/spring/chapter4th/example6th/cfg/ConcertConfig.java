package com.siwuxie095.spring.chapter4th.example6th.cfg;

import com.siwuxie095.spring.chapter4th.example6th.Audience;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 *
 *
 * 在 JavaConfig 中启用 AspectJ 注解的自动代理
 *
 * @author Jiajing Li
 * @date 2021-01-16 15:23:20
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan("com.siwuxie095.spring.chapter4th.example6th")
@EnableAspectJAutoProxy
public class ConcertConfig {

    @Bean
    public Audience audience() {
        return new Audience();
    }

}
