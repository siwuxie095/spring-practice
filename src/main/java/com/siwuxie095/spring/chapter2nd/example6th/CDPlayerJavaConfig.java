package com.siwuxie095.spring.chapter2nd.example6th;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiajing Li
 * @date 2020-12-25 08:27:50
 */
@SuppressWarnings("all")
@Configuration
public class CDPlayerJavaConfig {

    @Bean
    public CDPlayer cdPlayer(CompactDisc compactDisc) {
        return new CDPlayer(compactDisc);
    }

}
