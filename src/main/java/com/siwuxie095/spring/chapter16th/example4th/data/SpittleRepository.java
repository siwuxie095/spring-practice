package com.siwuxie095.spring.chapter16th.example4th.data;

import com.siwuxie095.spring.chapter16th.example4th.domain.Spittle;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-16 22:30:15
 */
@SuppressWarnings("all")
public interface SpittleRepository {

    List<Spittle> findRecentSpittles();

    List<Spittle> findSpittles(long max, int count);

    Spittle findOne(long id);

    void save(Spittle spittle);

}

