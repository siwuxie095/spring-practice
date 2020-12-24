package com.siwuxie095.spring.chapter2nd.example5th;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jiajing Li
 * @date 2020-12-24 22:48:41
 */
@SuppressWarnings("all")
public class DVDPlayer implements MediaPlayer {

    private CompactDisc cd;

    @Autowired
    public void setCd(CompactDisc cd) {
        this.cd = cd;
    }

    @Override
    public void play() {
        cd.play();
    }

}
