package com.siwuxie095.spring.chapter5th.example6th.data;

import com.siwuxie095.spring.chapter5th.example6th.Spitter;

/**
 * @author Jiajing Li
 * @date 2021-01-22 22:32:29
 */
@SuppressWarnings("all")
public interface SpitterRepository {

    Spitter save(Spitter spitter);

    Spitter findByUsername(String username);

}
