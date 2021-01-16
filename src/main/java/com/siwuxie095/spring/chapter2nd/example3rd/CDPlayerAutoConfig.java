package com.siwuxie095.spring.chapter2nd.example3rd;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ComponentScan 注解启用了组件扫描
 *
 * 为了展示 @ComponentScan 默认会扫描当前包以及其子包下的组件，这里没有创建一个 cfg 包来容纳当前类
 *
 * @author Jiajing Li
 * @date 2020-12-21 22:26:33
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan
public class CDPlayerAutoConfig {

}
