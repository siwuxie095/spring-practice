package com.siwuxie095.spring.chapter11th.example2nd.db;

import com.siwuxie095.spring.chapter11th.example2nd.domain.Spitter;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-24 22:57:35
 */
@SuppressWarnings("all")
public interface SpitterRepository {

    long count();

    Spitter save(Spitter spitter);

    Spitter findOne(long id);

    Spitter findByUsername(String username);

    List<Spitter> findAll();

}
