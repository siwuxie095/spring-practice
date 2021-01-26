package com.siwuxie095.spring.chapter6th.example5th.data;

import com.siwuxie095.spring.chapter6th.example5th.Spittle;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-01-26 23:00:08
 */
@SuppressWarnings("all")
public interface SpittleRepository {

    List<Spittle> findRecentSpittles();

    List<Spittle> findSpittles(long max, int count);

    Spittle findOne(long id);

    void save(Spittle spittle);

}
