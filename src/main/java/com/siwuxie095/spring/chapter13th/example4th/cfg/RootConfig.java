package com.siwuxie095.spring.chapter13th.example4th.cfg;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Jiajing Li
 * @date 2021-03-06 20:29:41
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan("com.siwuxie095.spring.chapter13th.example4th.db")
@Import({DataConfig.class, CachingConfig.class})
public class RootConfig {

}
