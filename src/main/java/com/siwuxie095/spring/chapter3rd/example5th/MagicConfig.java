package com.siwuxie095.spring.chapter3rd.example5th;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 条件化地配置 bean
 *
 * @author Jiajing Li
 * @date 2021-01-05 08:01:44
 */
@SuppressWarnings("all")
@Configuration
public class MagicConfig {

    @Bean
    @Conditional(MagicExistsCondition.class)
    public MagicBean magicBean() {
        return new MagicBean();
    }

}
