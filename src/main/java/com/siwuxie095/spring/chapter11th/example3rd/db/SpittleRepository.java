package com.siwuxie095.spring.chapter11th.example3rd.db;

import com.siwuxie095.spring.chapter11th.example3rd.domain.Spittle;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-25 21:46:09
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
