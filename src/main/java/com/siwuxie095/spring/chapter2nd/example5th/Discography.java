package com.siwuxie095.spring.chapter2nd.example5th;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2020-12-24 22:39:11
 */
@SuppressWarnings("all")
public class Discography implements MediaPlayer {

    private String artist;

    private List<CompactDisc> cds;

    public Discography(String artist, List<CompactDisc> cds) {
        this.artist = artist;
        this.cds = cds;
    }

    @Override
    public void play() {
        System.out.println("by " + artist);
        for (CompactDisc cd : cds) {
            cd.play();
        }
    }

}
