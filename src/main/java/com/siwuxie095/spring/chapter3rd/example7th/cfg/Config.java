package com.siwuxie095.spring.chapter3rd.example7th.cfg;

import com.siwuxie095.spring.chapter3rd.example7th.Notepad;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author Jiajing Li
 * @date 2021-01-07 08:26:33
 */
@SuppressWarnings("all")
@Configuration
public class Config {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Notepad notepad() {
        return new Notepad();
    }

}
