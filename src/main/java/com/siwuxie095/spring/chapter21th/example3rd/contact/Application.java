package com.siwuxie095.spring.chapter21th.example3rd.contact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Jiajing Li
 * @date 2021-04-03 10:48:28
 */
@SuppressWarnings("all")
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

