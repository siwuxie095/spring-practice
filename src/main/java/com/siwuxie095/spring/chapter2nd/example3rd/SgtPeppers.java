package com.siwuxie095.spring.chapter2nd.example3rd;

import org.springframework.stereotype.Component;

/**
 * 带有 @Component 注解的 CompactDisc 实现类 SgtPeppers
 *
 * @author Jiajing Li
 * @date 2020-12-21 22:05:44
 */
@SuppressWarnings("all")
@Component
public class SgtPeppers implements CompactDisc {

    private String title = "Sgt. Pepper's Lonely Hearts Club Band";
    private String artist = "The Beatles";

    @Override
    public void play() {
        System.out.println("Playing " + title + " by " + artist);
    }
}
