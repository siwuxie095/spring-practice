package com.siwuxie095.spring.chapter6th.example6th.data;

import com.siwuxie095.spring.chapter6th.example6th.Spittle;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-01-27 21:56:16
 */
@SuppressWarnings("all")
public interface SpittleRepository {

    List<Spittle> findRecentSpittles();

    List<Spittle> findSpittles(long max, int count);

    Spittle findOne(long id);

    void save(Spittle spittle);

}

