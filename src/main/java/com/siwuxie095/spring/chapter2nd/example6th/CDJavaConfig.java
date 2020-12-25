package com.siwuxie095.spring.chapter2nd.example6th;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiajing Li
 * @date 2020-12-25 08:27:29
 */
@SuppressWarnings("all")
@Configuration
public class CDJavaConfig {

    @Bean
    public CompactDisc compactDisc() {
        return new SgtPeppers();
    }

}
