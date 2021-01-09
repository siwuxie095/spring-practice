package com.siwuxie095.spring.chapter3rd.example9th.cfg;

import com.siwuxie095.spring.chapter3rd.example9th.BlankDisc;
import com.siwuxie095.spring.chapter3rd.example9th.CompactDisc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

/**
 * 使用 @PropertySource 注解和 Environment
 *
 * @author Jiajing Li
 * @date 2021-01-09 13:50:40
 */
@SuppressWarnings("all")
@Configuration
@PropertySource("file:src/main/java/com/siwuxie095/spring/chapter3rd/example9th/res/app.properties")
public class ExpressiveConfig {

    @Autowired
    private Environment env;

    @Bean
    public BlankDisc disc() {
        return new BlankDisc(
                env.getProperty("disc.title"),
                env.getProperty("disc.artist"));
    }

}
