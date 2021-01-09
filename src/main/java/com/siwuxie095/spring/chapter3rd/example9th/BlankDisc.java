package com.siwuxie095.spring.chapter3rd.example9th;

/**
 * @author Jiajing Li
 * @date 2021-01-09 13:49:21
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
