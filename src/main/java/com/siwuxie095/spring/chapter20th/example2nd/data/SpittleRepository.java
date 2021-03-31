package com.siwuxie095.spring.chapter20th.example2nd.data;

import com.siwuxie095.spring.chapter20th.example2nd.domain.Spittle;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-31 08:22:57
 */
@SuppressWarnings("all")
public interface SpittleRepository {

    List<Spittle> findRecentSpittles();

    List<Spittle> findSpittles(long max, int count);

    Spittle findOne(long id);

    void save(Spittle spittle);

}
