package com.siwuxie095.spring.chapter4th.example6th.cfg;

import com.siwuxie095.spring.chapter4th.example6th.BlankDisc;
import com.siwuxie095.spring.chapter4th.example6th.CompactDisc;
import com.siwuxie095.spring.chapter4th.example6th.TrackCounter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置 TrackCount 记录每个磁道播放的次数
 *
 * @author Jiajing Li
 * @date 2021-01-16 16:20:53
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan("com.siwuxie095.spring.chapter4th.example6th")
@EnableAspectJAutoProxy
public class TrackCounterConfig {

    @Bean
    public CompactDisc compactDisc() {
        BlankDisc blankDisc = new BlankDisc();
        blankDisc.setTitle("Sgt. Pepper's Lonely Hearts Club Band");
        blankDisc.setArtist("The Beatles");
        List<String> tracks = new ArrayList<>();
        tracks.add("Sgt. Pepper's Lonely Hearts Club Band");
        tracks.add("With a Little Help from My Friends");
        tracks.add("Lucy in the Sky with Diamonds");
        tracks.add("Getting Better");
        tracks.add("Fixing a Hole");
        // ...other tracks omitted for brevity...
        blankDisc.setTracks(tracks);
        return blankDisc;

    }

    @Bean
    public TrackCounter trackCounter() {
        return new TrackCounter();
    }

}
