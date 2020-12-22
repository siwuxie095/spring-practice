package com.siwuxie095.spring.chapter2nd.example4th;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiajing Li
 * @date 2020-12-22 22:05:35
 */
@SuppressWarnings("all")
@Configuration
public class CDPlayerJavaConfig {

    @Bean
    public CompactDisc compactDisc() {
        return new SgtPeppers();
    }

    @Bean
    public CDPlayer cdPlayer(CompactDisc compactDisc) {
        return new CDPlayer(compactDisc);
    }

}
