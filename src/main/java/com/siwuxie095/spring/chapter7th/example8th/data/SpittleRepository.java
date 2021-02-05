package com.siwuxie095.spring.chapter7th.example8th.data;

import com.siwuxie095.spring.chapter7th.example8th.Spittle;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-05 20:40:39
 */
@SuppressWarnings("all")
public interface SpittleRepository {

    List<Spittle> findRecentSpittles();

    List<Spittle> findSpittles(long max, int count);

    Spittle findOne(long id);

    void save(Spittle spittle);

}
