package com.siwuxie095.spring.chapter2nd.example6th;

/**
 * @author Jiajing Li
 * @date 2020-12-25 08:24:57
 */
@SuppressWarnings("all")
public class SgtPeppers implements CompactDisc {

    private String title = "Sgt. Pepper's Lonely Hearts Club Band";
    private String artist = "The Beatles";

    @Override
    public void play() {
        System.out.println("Playing " + title + " by " + artist);
    }

}
