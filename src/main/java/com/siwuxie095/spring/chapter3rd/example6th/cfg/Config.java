package com.siwuxie095.spring.chapter3rd.example6th.cfg;

import com.siwuxie095.spring.chapter3rd.example6th.Dessert;
import com.siwuxie095.spring.chapter3rd.example6th.IceCream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiajing Li
 * @date 2021-01-06 23:00:13
 */
@SuppressWarnings("all")
@Configuration
public class Config {

    @Bean
    public Dessert iceCream() {
        return new IceCream();
    }

}
