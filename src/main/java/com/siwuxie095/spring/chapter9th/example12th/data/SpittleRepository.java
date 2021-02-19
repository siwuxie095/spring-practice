package com.siwuxie095.spring.chapter9th.example12th.data;

import com.siwuxie095.spring.chapter9th.example12th.Spittle;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-19 08:21:49
 */
@SuppressWarnings("all")
public interface SpittleRepository {

    List<Spittle> findRecentSpittles();

    List<Spittle> findSpittles(long max, int count);

    Spittle findOne(long id);

    void save(Spittle spittle);

}

