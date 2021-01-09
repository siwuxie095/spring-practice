package com.siwuxie095.spring.chapter3rd.example10th;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Jiajing Li
 * @date 2021-01-09 15:29:56
 */
@SuppressWarnings("all")
public class BlankDisc implements CompactDisc {

    private String title;
    private String artist;

    public BlankDisc(
            @Value("#{systemProperties['disc.title']}") String title,
            @Value("#{systemProperties['disc.artist']}") String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public void play() {
        System.out.println("Playing " + title + " by " + artist);
    }

}
