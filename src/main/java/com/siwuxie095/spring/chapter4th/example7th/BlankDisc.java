package com.siwuxie095.spring.chapter4th.example7th;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-01-17 21:58:50
 */
public class BlankDisc implements CompactDisc {

    private String title;
    private String artist;
    private List<String> tracks;

    public BlankDisc(String title, String artist, List<String> tracks) {
        this.title = title;
        this.artist = artist;
        this.tracks = tracks;
    }

    public BlankDisc() {
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
        for (int i = 0; i < tracks.size(); i++) {
            playTrack(i);
        }
    }

    @Override
    public void playTrack(int trackNumber) {
        System.out.println("-Track: " + tracks.get(trackNumber));
    }

}
