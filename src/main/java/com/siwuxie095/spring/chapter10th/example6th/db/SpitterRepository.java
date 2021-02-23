package com.siwuxie095.spring.chapter10th.example6th.db;

import com.siwuxie095.spring.chapter10th.example6th.domain.Spitter;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-23 20:56:47
 */
@SuppressWarnings("all")
public interface SpitterRepository {

    long count();

    Spitter save(Spitter spitter);

    Spitter findOne(long id);

    Spitter findByUsername(String username);

    List<Spitter> findAll();

}
