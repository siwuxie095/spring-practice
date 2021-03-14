package com.siwuxie095.spring.chapter15th.example5th.service;

import com.siwuxie095.spring.chapter15th.example5th.domain.Spitter;
import com.siwuxie095.spring.chapter15th.example5th.domain.Spittle;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-14 20:55:32
 */
@SuppressWarnings("all")
public interface SpitterService {

    List<Spittle> getRecentSpittles(int count);

    void saveSpittle(Spittle spittle);

    void saveSpitter(Spitter spitter);

    Spitter getSpitter(long id);

    void startFollowing(Spitter follower, Spitter followee);

    List<Spittle> getSpittlesForSpitter(Spitter spitter);

    List<Spittle> getSpittlesForSpitter(String username);

    Spitter getSpitter(String username);

    Spittle getSpittleById(long id);

    void deleteSpittle(long id);

    List<Spitter> getAllSpitters();

}

