package com.siwuxie095.spring.chapter6th.example5th.data;

import com.siwuxie095.spring.chapter6th.example5th.Spitter;

/**
 * @author Jiajing Li
 * @date 2021-01-26 22:59:34
 */
@SuppressWarnings("all")
public interface SpitterRepository {

    Spitter save(Spitter spitter);

    Spitter findByUsername(String username);

}
