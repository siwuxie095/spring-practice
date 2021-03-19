package com.siwuxie095.spring.chapter16th.example5th.data;

import com.siwuxie095.spring.chapter16th.example5th.domain.Spittle;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-18 22:45:14
 */
@SuppressWarnings("all")
public interface SpittleRepository {

    List<Spittle> findRecentSpittles();

    List<Spittle> findSpittles(long max, int count);

    Spittle findOne(long id);

    Spittle save(Spittle spittle);

}

