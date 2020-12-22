package com.siwuxie095.spring.chapter2nd.example4th;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jiajing Li
 * @date 2020-12-22 22:07:48
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
