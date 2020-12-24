package com.siwuxie095.spring.chapter2nd.example5th;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2020-12-24 22:25:05
 */
@SuppressWarnings("all")
public class MultiBlankDisc implements CompactDisc {

    private String title;
    private String artist;
    private List<String> tracks;

    public MultiBlankDisc(String title, String artist, List<String> tracks) {
        this.title = title;
        this.artist = artist;
        this.tracks = tracks;
    }

    public MultiBlankDisc() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTracks(List<String> tracks) {
        this.tracks = tracks;
    }

    @Override
    public void play() {
        System.out.println("Playing " + title + " by " + artist);
        for (String track : tracks) {
            System.out.println("-Track: " + track);
        }
    }

}
