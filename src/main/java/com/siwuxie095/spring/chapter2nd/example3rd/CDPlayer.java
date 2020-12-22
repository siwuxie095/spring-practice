package com.siwuxie095.spring.chapter2nd.example3rd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通过自动装配，将一个 CompactDisc 注入到 CDPlayer 之中
 *
 * @author Jiajing Li
 * @date 2020-12-22 08:37:26
 */
@SuppressWarnings("all")
@Component
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
