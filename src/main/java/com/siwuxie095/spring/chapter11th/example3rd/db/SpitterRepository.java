package com.siwuxie095.spring.chapter11th.example3rd.db;

import com.siwuxie095.spring.chapter11th.example3rd.domain.Spitter;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-25 21:45:32
 */
@SuppressWarnings("all")
public interface SpitterRepository {

    long count();

    Spitter save(Spitter spitter);

    Spitter findOne(long id);

    Spitter findByUsername(String username);

    List<Spitter> findAll();

}
