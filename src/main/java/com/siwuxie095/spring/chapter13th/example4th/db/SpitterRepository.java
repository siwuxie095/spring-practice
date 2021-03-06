package com.siwuxie095.spring.chapter13th.example4th.db;

import com.siwuxie095.spring.chapter13th.example4th.domain.Spitter;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-06 20:24:52
 */
@SuppressWarnings("all")
public interface SpitterRepository {

    long count();

    Spitter save(Spitter spitter);

    Spitter findOne(long id);

    Spitter findByUsername(String username);

    List<Spitter> findAll();

}
