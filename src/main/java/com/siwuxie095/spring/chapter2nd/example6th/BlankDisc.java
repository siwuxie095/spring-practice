package com.siwuxie095.spring.chapter2nd.example6th;

/**
 * @author Jiajing Li
 * @date 2020-12-25 08:27:00
 */
@SuppressWarnings("all")
public class BlankDisc implements CompactDisc {

    private String title;
    private String artist;

    public BlankDisc(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public void play() {
        System.out.println("Playing " + title + " by " + artist);
    }
}
