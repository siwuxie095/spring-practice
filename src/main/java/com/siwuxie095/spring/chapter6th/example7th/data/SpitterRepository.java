package com.siwuxie095.spring.chapter6th.example7th.data;

import com.siwuxie095.spring.chapter6th.example7th.Spitter;

/**
 * @author Jiajing Li
 * @date 2021-01-28 22:09:59
 */
@SuppressWarnings("all")
public interface SpitterRepository {

    Spitter save(Spitter spitter);

    Spitter findByUsername(String username);

}
