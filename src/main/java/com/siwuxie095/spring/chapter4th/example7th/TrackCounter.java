package com.siwuxie095.spring.chapter4th.example7th;

import java.util.HashMap;
import java.util.Map;

/**
 * 无注解的 TrackCounter
 *
 * @author Jiajing Li
 * @date 2021-01-17 21:59:39
 */
@SuppressWarnings("all")
public class TrackCounter {

    private Map<Integer, Integer> trackCounts = new HashMap<>();

    public void countTrack(int trackNumber) {
        int currentCount = getPlayCount(trackNumber);
        trackCounts.put(trackNumber, currentCount + 1);
    }

    public int getPlayCount(int trackNumber) {
        return trackCounts.containsKey(trackNumber) ? trackCounts.get(trackNumber) : 0;
    }

}
