package com.siwuxie095.spring.chapter7th.example8th.data;

import com.siwuxie095.spring.chapter7th.example8th.Spitter;

/**
 * @author Jiajing Li
 * @date 2021-02-05 20:37:38
 */
@SuppressWarnings("all")
public interface SpitterRepository {

    Spitter save(Spitter spitter);

    Spitter findByUsername(String username);

}

