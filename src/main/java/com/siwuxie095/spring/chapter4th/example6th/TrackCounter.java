package com.siwuxie095.spring.chapter4th.example6th;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用参数化的通知来记录磁道播放的次数
 *
 * @author Jiajing Li
 * @date 2021-01-16 16:08:08
 */
@SuppressWarnings("all")
@Aspect
public class TrackCounter {

    private Map<Integer, Integer> trackCounts = new HashMap<>();

    // 通知 playTrack() 方法
    @Pointcut("execution(* com.siwuxie095.spring.chapter4th.example6th.CompactDisc.playTrack(int)) " +
            "&& args(trackNumber)")
    public void trackPlayed(int trackNumber) {}

    // 在播放前，为该磁道计数
    @Before("trackPlayed(trackNumber)")
    public void countTrack(int trackNumber) {
        int currentCount = getPlayCount(trackNumber);
        trackCounts.put(trackNumber, currentCount + 1);
    }

    public int getPlayCount(int trackNumber) {
        return trackCounts.containsKey(trackNumber) ? trackCounts.get(trackNumber) : 0;
    }

}
