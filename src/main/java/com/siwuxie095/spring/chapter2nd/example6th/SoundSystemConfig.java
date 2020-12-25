package com.siwuxie095.spring.chapter2nd.example6th;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Jiajing Li
 * @date 2020-12-25 08:28:08
 */
@SuppressWarnings("all")
@Configuration
@Import(CDPlayerJavaConfig.class)
@ImportResource("file:src/main/java/com/siwuxie095/spring/chapter2nd/example6th/res/cd-config.xml")
public class SoundSystemConfig {

}
