package com.siwuxie095.spring.chapter15th.example6th.service;

import com.siwuxie095.spring.chapter15th.example6th.domain.Spitter;
import com.siwuxie095.spring.chapter15th.example6th.domain.Spittle;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-14 21:46:34
 */
@SuppressWarnings("all")
@Service
public class SpitterServiceImpl implements SpitterService {

    @Override
    public List<Spittle> getRecentSpittles(int count) {
        return null;
    }

    @Override
    public void saveSpittle(Spittle spittle) {

    }

    @Override
    public void saveSpitter(Spitter spitter) {

    }

    @Override
    public Spitter getSpitter(long id) {
        return null;
    }

    @Override
    public void startFollowing(Spitter follower, Spitter followee) {

    }

    @Override
    public List<Spittle> getSpittlesForSpitter(Spitter spitter) {
        return null;
    }

    @Override
    public List<Spittle> getSpittlesForSpitter(String username) {
        return null;
    }

    @Override
    public Spitter getSpitter(String username) {
        return null;
    }

    @Override
    public Spittle getSpittleById(long id) {
        return null;
    }

    @Override
    public void deleteSpittle(long id) {

    }

    @Override
    public List<Spitter> getAllSpitters() {
        return null;
    }

}

