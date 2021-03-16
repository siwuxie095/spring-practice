package com.siwuxie095.spring.chapter16th.example3rd.data;

import com.siwuxie095.spring.chapter16th.example3rd.domain.Spittle;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-16 21:45:10
 */
@SuppressWarnings("all")
public interface SpittleRepository {

    List<Spittle> findRecentSpittles();

    List<Spittle> findSpittles(long max, int count);

    Spittle findOne(long id);

    void save(Spittle spittle);

}

