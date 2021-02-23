package com.siwuxie095.spring.chapter10th.example6th.db;

import com.siwuxie095.spring.chapter10th.example6th.domain.Spittle;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-23 20:57:55
 */
@SuppressWarnings("all")
public interface SpittleRepository {

    long count();

    List<Spittle> findRecent();

    List<Spittle> findRecent(int count);

    Spittle findOne(long id);

    Spittle save(Spittle spittle);

    List<Spittle> findBySpitterId(long spitterId);

    void delete(long id);

}
