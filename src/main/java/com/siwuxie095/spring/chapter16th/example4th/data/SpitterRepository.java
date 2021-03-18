package com.siwuxie095.spring.chapter16th.example4th.data;

import com.siwuxie095.spring.chapter16th.example4th.domain.Spitter;

/**
 * @author Jiajing Li
 * @date 2021-03-16 22:29:43
 */
@SuppressWarnings("all")
public interface SpitterRepository {

    Spitter save(Spitter spitter);

    Spitter findByUsername(String username);

}

