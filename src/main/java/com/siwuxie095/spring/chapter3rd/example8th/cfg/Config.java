package com.siwuxie095.spring.chapter3rd.example8th.cfg;

import com.siwuxie095.spring.chapter3rd.example8th.BlankDisc;
import com.siwuxie095.spring.chapter3rd.example8th.CompactDisc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiajing Li
 * @date 2021-01-08 21:23:49
 */
@SuppressWarnings("all")
@Configuration
public class Config {

    @Bean
    public CompactDisc compactDisc() {
        return new BlankDisc("Sgt. Pepper's Lonely Hearts Club Band", "The Beatles");
    }

}
