package com.siwuxie095.spring.chapter6th.example6th.data;

import com.siwuxie095.spring.chapter6th.example6th.Spitter;

/**
 * @author Jiajing Li
 * @date 2021-01-27 21:54:32
 */
@SuppressWarnings("all")
public interface SpitterRepository {

    Spitter save(Spitter spitter);

    Spitter findByUsername(String username);

}
