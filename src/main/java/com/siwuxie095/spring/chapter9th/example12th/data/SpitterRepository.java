package com.siwuxie095.spring.chapter9th.example12th.data;

import com.siwuxie095.spring.chapter9th.example12th.Spitter;

/**
 * @author Jiajing Li
 * @date 2021-02-19 08:21:12
 */
@SuppressWarnings("all")
public interface SpitterRepository {

    Spitter save(Spitter spitter);

    Spitter findByUsername(String username);

}
