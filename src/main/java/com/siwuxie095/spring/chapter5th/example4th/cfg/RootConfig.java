package com.siwuxie095.spring.chapter5th.example4th.cfg;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Jiajing Li
 * @date 2021-01-20 21:56:07
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan(basePackages = {"com.siwuxie095.spring.chapter5th.example4th"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)
        })
public class RootConfig {


}
