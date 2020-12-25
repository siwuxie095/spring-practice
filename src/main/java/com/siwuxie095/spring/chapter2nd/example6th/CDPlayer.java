package com.siwuxie095.spring.chapter2nd.example6th;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jiajing Li
 * @date 2020-12-25 08:25:49
 */
@SuppressWarnings("all")
public class CDPlayer implements MediaPlayer {

    private CompactDisc cd;

    @Autowired
    public CDPlayer(CompactDisc cd) {
        this.cd = cd;
    }

    @Override
    public void play() {
        cd.play();
    }

}
